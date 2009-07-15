package br.ufpb.ppm.test;

import org.junit.Test;
import static org.junit.Assert.*;

import br.ufpb.ppm.Ppm;

public class PpmTest {

	@Test
	public void getCodeOk() {
		byte test = (byte)255;
		String code = Ppm.getCode(test);
		assertEquals("11111111", code);
	}
	
	@Test
	public void getCodeNegative() {
		byte test = (byte)-127; //129 usando todos os bits
		String code = Ppm.getCode(test);
		assertEquals("10000001", code); //código binário de 129
	}
	
}
