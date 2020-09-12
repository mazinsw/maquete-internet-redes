package test;

import static org.junit.Assert.*;

import org.junit.Test;

import common.Average;

public class TestAverage {

	@Test
	public void testAvg() {
		Average avg = new Average();
		avg.add(1);
		assertEquals(1, avg.get(), 0.001);
		avg.add(2);
		assertEquals(1.5, avg.get(), 0.001);
		avg.add(3);
		assertEquals(2, avg.get(), 0.001);
		avg.add(0);
		assertEquals(1.5, avg.get(), 0.001);
		avg.add(14);
		assertEquals(4, avg.get(), 0.001);
	}
	
	@Test
	public void testCount() {
		Average avg = new Average();
		assertEquals(0, avg.getCount());
		avg.add(9);
		avg.add(6);
		avg.add(3);
		avg.add(2);
		assertEquals(4, avg.getCount());
		assertEquals(5, avg.get(), 0.001);
	}

}
