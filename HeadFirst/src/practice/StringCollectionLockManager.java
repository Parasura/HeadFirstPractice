package practice;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Non reentrant locking of string collection handled by this class.
 * 
 * TODO Reentrant locking to be implemented.
 *
 */
public class StringCollectionLockManager {

	/**
	 * Name of the module that creates the instance of
	 * StringCollectionLockManager. This is used for internally maintaining the
	 * instances.
	 */
	private final String name;

	private final Map<String, ThreadWrapper> map = new HashMap<>();

	private final AtomicLong hits = new AtomicLong(0);

	private final AtomicLong misses = new AtomicLong(0);

	/**
	 * StringCollectionLockManager is wrapped in WeakReference so that it shall
	 * be GC'd if there are no live references.
	 */
	private static final ConcurrentHashMap<String, WeakReference<StringCollectionLockManager>> instances = new ConcurrentHashMap<>();

	// private final ReentrantLock lock = new ReentrantLock();
	private final Object LOCK = new Object();

	private final ThreadLocal<Collection<String>> threadLocalCollection = new ThreadLocal<>();

	private final ThreadLocal<ThreadWrapper> thrdWrpr = new ThreadLocal<ThreadWrapper>() {
		protected ThreadWrapper initialValue() {
			return new ThreadWrapper(Thread.currentThread());
		}
	};

	Semaphore semaPhore = new Semaphore(1);

	public StringCollectionLockManager(String name) {
		this.name = name;
		instances.put(name, new WeakReference<StringCollectionLockManager>(this));
	}

	/**
	 * Obtains lock for the strings given in the collection blocking till the
	 * lock is obtained.
	 * 
	 * @param stringCollection
	 * @throws InterruptedException
	 * 
	 * @throws ReentrantLockException
	 *             if the lock for a string in the collection is already held by
	 *             the same thread.
	 */
	public void lock(Collection<String> stringCollection) throws InterruptedException {
		Thread ct = Thread.currentThread();
		while (true) {
			ThreadWrapper lt = getLockingThread(stringCollection);
			if (ct == lt.t) {
				semaPhore.acquire();
				hits.incrementAndGet();
				break;
			} else {
				misses.incrementAndGet();
			}

			synchronized (lt) {
				while (!lt.finish) {
					try {
						lt.wait();
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	/**
	 * Tries to own the lock for the strings given in the collection returning
	 * false if not able to obtain lock.
	 * 
	 * @param stringCollection
	 * 
	 * @return boolean indicating whether the lock has been obtained.
	 * 
	 * @throws ReentrantLockException
	 *             if the lock for a string in the collection is already held by
	 *             the same thread.
	 */
	public boolean tryLock(Collection<String> stringCollection) {
		Thread ct = Thread.currentThread();
		ThreadWrapper lt = getLockingThread(stringCollection);
		boolean b = (ct == lt.t);

		if (b)
			hits.incrementAndGet();
		else
			misses.incrementAndGet();

		return b;
	}

	private ThreadWrapper getLockingThread(Collection<String> stringCollection) {
		synchronized (LOCK) {
			Collection<String> existingCollection = threadLocalCollection.get();
			if (existingCollection != null)
				throw new ReentrantLockException(existingCollection);

			ThreadWrapper ct = thrdWrpr.get();
			ct.finish = false; // resetting the value if the value is already
								// set.

			for (String str : stringCollection) {
				if (!map.containsKey(str))
					continue;

				/*
				 * Object upon which the other threads shall wait. Some other
				 * thread holds the lock. The threads which need lock can wait
				 * on the returned object.
				 */
				return map.get(str);
			}

			for (String str : stringCollection) {
				map.put(str, ct);
			}

			/*
			 * Setting thread name and locked time again to ensure that the
			 * thread local value has the updated details in case the same
			 * thread is used for getting lock(not reentrant) i.e TPE workers.
			 */
			ct.threadName = ct.t.getName();
			ct.lockedTime = System.currentTimeMillis();

			threadLocalCollection.set(stringCollection);

			return ct;
		}
	}

	public void unlock() {
		Collection<String> stringCollection = threadLocalCollection.get();
		if (stringCollection == null)
			return;

		threadLocalCollection.set(null); // reinitializing the thread local
											// variable;

		ThreadWrapper ct = thrdWrpr.get();

		for (String str : stringCollection) {
			map.remove(str);
		}
		ct.finish = true;
		//ct.notifyAll();
		semaPhore.release();

	}

	public String getName() {
		return name;
	}

	private static class ThreadWrapper {
		Thread t;
		volatile boolean finish;
		String threadName;
		long lockedTime;

		ThreadWrapper(Thread t) {
			this.t = t;
			this.threadName = t.getName();
			this.lockedTime = System.currentTimeMillis();
		}

		@Override
		public String toString() {
			return threadName + " locked at " + new Date(lockedTime);
		}
	}

	public static class ReentrantLockException extends RuntimeException {
		public ReentrantLockException(Collection<String> collection) {
			super("Reentrant locking is not supported!!" + "\n" + Thread.currentThread() + " already holds a lock for another collection "
					+ collection);
		}
	}

}