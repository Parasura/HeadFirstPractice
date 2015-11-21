package practice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SemaPhore implements Runnable {
	
	Semaphore sm = new Semaphore(1);

	public static void main(String[] args) {
		Thread t1 = new Thread(new SemaPhore(), "1st");
		Thread t2 = new Thread(new SemaPhore(), "2nd");
		t1.start();
		t2.start();
	}

	@Override
	public void run() {

		/*for (int i = 0; i < 10; i++) {
			lock();
			System.out.println("Executed by " + Thread.currentThread().getName());
			unlock();
		}*/
		for (int i = 0; i < 10; i++) {
			StringCollectionLockManager st = new StringCollectionLockManager("");
			List<String> lt = new ArrayList<String>();
			lt.add("parasuraman");
			try {
				st.lock(lt);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Executed by " + Thread.currentThread().getName());
			st.unlock();
		}
	}
	

	private void unlock() {
		sm.release();
	}

	private void lock() {
		try {
			sm.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
