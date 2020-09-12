package common;

/** 
  * The Timer class allows a graceful exit when an application
  * is stalled due to a networking timeout. Once the timer is
  * set, it must be cleared via the reset() method, or the
  * timeout() method is called.
  * <p>
  * The timeout length is customizable, by changing the 'length'
  * property, or through the constructor. The length represents
  * the length of the timer in milliseconds.
  *
  * @author	David Reilly
  */
public class Timer extends Thread
{
	/** Rate at which timer is checked */
	protected int m_rate = 10;
	
	/** Length of timeout */
	private int m_length;

	/** Time elapsed */
	private int m_elapsed;
	
	/** Timeout method */
	private TimerListener m_task;
	
	private boolean started;
	
	private boolean executed;
	private boolean stopped;

	/**
	  * Creates a timer of a specified length
	  * @param	length	Length of time before timeout occurs
	  */
	public Timer ( int length , TimerListener task )
	{
		// Assign to member variable
		m_length = length;

		// Set time elapsed
		m_elapsed = 0;
		
		// Assign task method
		m_task = task;
	}

	public synchronized void setLength(int length ) {
		m_length = length;
		reset();
	}
	
	/** Resets the timer back to zero */
	public synchronized void reset()
	{
		m_elapsed = 0;
		executed = false;
	}

	/** Performs timer specific code */
	public void run()
	{
		// Keep looping
		for (;!stopped;)
		{
			// Put the timer to sleep
			try
			{ 
				Thread.sleep(m_rate);
			}
			catch (InterruptedException ioe) 
			{
				continue;
			}

			// Use 'synchronized' to prevent conflicts
			synchronized ( this )
			{
				// Increment time remaining
				m_elapsed += m_rate;

				// Check to see if the time has been exceeded
				if (m_elapsed > m_length && !executed)
				{
					executed = true;
					// Trigger a timeout
					m_task.onTimer();
				}
			}

		}
	}
	
	@Override
	public synchronized void start() {
		if(started)
			reset();
		else
			super.start();
		started = true;
	}
	
	public synchronized void cancel() {
		m_elapsed = 0;
		executed = true;
		stopped = true;
	}
}