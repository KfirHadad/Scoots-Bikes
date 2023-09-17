public class BoundedQueue<T> extends Queue<T> {

	private final int capacity = 5;

	@Override
	public synchronized void add(T item) {
		try {
			while (getSize() >= capacity) {
				wait(); // Wait if the buffer is full.
			}
			super.add(item); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean isFull() {
		return getSize() == capacity;
	}
}