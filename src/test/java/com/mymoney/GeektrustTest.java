package com.mymoney;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class GeektrustTest {

	private final Geektrust geektrust = new Geektrust();

	@Test
	void parseAndProcessFileCanRebalanceTest() {
		assertEquals("10593 7897 2272" + StringUtils.LF + "23619 11809 3936",
				geektrust.parseAndProcessFile("/Users/nirupam/Downloads/portfolio/src/main/resources/input1.txt"));
	}

	@Test
	void parseAndProcessFileCannotRebalanceTest() {
		assertEquals("15937 14552 6187" + StringUtils.LF + "23292 16055 7690" + StringUtils.LF + "CANNOT_REBALANCE",
				geektrust.parseAndProcessFile("/Users/nirupam/Downloads/portfolio/src/main/resources/input2.txt"));
	}
}
