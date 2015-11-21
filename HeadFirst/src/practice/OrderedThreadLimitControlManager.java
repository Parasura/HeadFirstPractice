package practice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author nishanth
 * 
 * The purpose is to limit the number of threads that can be allowed to cross the 
 * lock() method of an instance of this class based on the value specified {@code maxThreadCount}.
 * 
 * Other threads will go to wait state
 * 
 * When a thread which has crossed the lock() method ,calls unlock() then the first thread which was put into waiting state 
 * will be awakened and allowed to cross the lock method
 * 
 * Reenterant Lock is allowed
 *
 */
public class OrderedThreadLimitControlManager 
{
	
	
	private Map<Long, AtomicInteger> threadIDReEnterantLockCountMap;
	
	private Semaphore semaphore;

	public OrderedThreadLimitControlManager(int maxThreadCount) {
		super();
		this.threadIDReEnterantLockCountMap = new ConcurrentHashMap<Long, AtomicInteger>();
		this.semaphore = new Semaphore(maxThreadCount, true);
	}
	
	public void lock()
	{
		long ctID = Thread.currentThread().getId();
		AtomicInteger reEnterantLockCount = threadIDReEnterantLockCountMap.get(ctID);
		if(reEnterantLockCount == null)
		{
			try 
			{
				semaphore.acquire();
			} 
			catch (InterruptedException e) 
			{
			}
			
			threadIDReEnterantLockCountMap.put(ctID, new AtomicInteger(1));
			
		//	logger.trace("Adding to map : {}", ctName);
		}
		else
		{
			int incrementedCount = reEnterantLockCount.incrementAndGet();
		}
	}
	
	public void unlock()
	{
		long ctID = Thread.currentThread().getId();
		AtomicInteger reEnterantLockCount = threadIDReEnterantLockCountMap.get(ctID);
			
		if(reEnterantLockCount == null)
		{
			throw new UnsupportedUnlockException(
					"Trying to call Unlock before Calling lock by Thread :"+Thread.currentThread().getName());
		}
		
		int decreasedCount = reEnterantLockCount.decrementAndGet();
		if(decreasedCount == 0)
		{
			threadIDReEnterantLockCountMap.remove(ctID);
			
		//	logger.trace("Removing from map : {}", ctName);
			semaphore.release();
		}
		
	}
	
	public class UnsupportedUnlockException extends RuntimeException
	{
		
		private static final long serialVersionUID = 1L;

		public UnsupportedUnlockException(String msg) 
		{
			super(msg);
		}
	
	}
	
}
