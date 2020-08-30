package synchronization;

public interface Lock {
	public static final int FALSE = 0;
	public static final int TRUE = 1;

	public void acquire(int tid);
	public void release(int tid);
}
