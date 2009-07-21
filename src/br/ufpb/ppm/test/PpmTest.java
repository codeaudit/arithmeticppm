package br.ufpb.ppm.test;

import org.junit.Test;
import static org.junit.Assert.*;

import br.ufpb.ppm.PpmCod;

public class PpmTest {

	@Test
	public void getCodeOk() {
		byte test = (byte)255;
		String code = PpmCod.getCode(test);
		assertEquals("11111111", code);
	}
	
	@Test
	public void getCodeNegative() {
		byte test = (byte)-127; //129 usando todos os bits
		String code = PpmCod.getCode(test);
		assertEquals("10000001", code); //código binário de 129
	}
	
	@Test
	public void splitCodeTest1() {
		String code = "10011100";
		String[] result = PpmCod.splitCode(code, 1);
		assertEquals("1", result[0]);
		assertEquals("0", result[1]);
		assertEquals("0", result[2]);
		assertEquals("1", result[3]);
		assertEquals("1", result[4]);
		assertEquals("1", result[5]);
		assertEquals("0", result[6]);
		assertEquals("0", result[7]);
	}
	
	@Test
	public void splitCodeTest2() {
		String code = "10011100";
		String[] result = PpmCod.splitCode(code, 2);
		assertEquals("10", result[0]);
		assertEquals("01", result[1]);
		assertEquals("11", result[2]);
		assertEquals("00", result[3]);
	}
	
	@Test
	public void splitCodeTest4() {
		String code = "10011100";
		String[] result = PpmCod.splitCode(code, 4);
		assertEquals("1001", result[0]);
		assertEquals("1100", result[1]);
	}
	
	@Test
	public void splitCodeTest8() {
		String code = "10011100";
		String[] result = PpmCod.splitCode(code, 8);
		assertEquals("10011100", result[0]);
	}
	
	@Test
	public void teste() {
		for (int i = 0; i < 256; i++)
			System.out.print((char)i);
		System.out.println();
		for (int i = 256; i < 512; i++)
			System.out.print((char)i);
		System.out.println();
		System.out.println((char)-1);
	}
}
