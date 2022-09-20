package com.mymoney;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class GeektrustTest {

	private final Geektrust geektrust = new Geektrust();

	@Test
	void parseAndProcessFileCanRebalanceTest() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("input1.txt");
		File file = new File(url.getPath());
		assertEquals("10593 7897 2272" + StringUtils.LF + "23619 11809 3936",
				geektrust.parseAndProcessFile(file));
	}

	@Test
	void parseAndProcessFileCannotRebalanceTest() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("input2.txt");
		File file = new File(url.getPath());
		assertEquals("15937 14552 6187" + StringUtils.LF + "23292 16055 7690" + StringUtils.LF + "CANNOT_REBALANCE",
				geektrust.parseAndProcessFile(file));
	}
}
