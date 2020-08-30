package synchronization;

public interface Barrier {
	public abstract void await() throws InterruptedException;
}
