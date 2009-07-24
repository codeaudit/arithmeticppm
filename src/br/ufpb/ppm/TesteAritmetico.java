package br.ufpb.ppm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.colloquial.arithcode.ArithDecoder;
import com.colloquial.arithcode.ArithEncoder;
import com.colloquial.arithcode.BitInput;
import com.colloquial.arithcode.BitOutput;

public class TesteAritmetico {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		ArithEncoder aritmetico;
		FileOutputStream aritmeticoteste = new FileOutputStream("teste.txt");
		aritmetico = new ArithEncoder (new BitOutput(aritmeticoteste));
		
		ArithDecoder decodificador;	
		FileInputStream testedecoder = new FileInputStream("teste.txt");
		
		try {
			aritmetico.encode(1, 3, 10);
			aritmetico.encode(5, 9, 13);
			aritmetico.flush();
			aritmeticoteste.flush();
			aritmetico.close();
			aritmeticoteste.close();

			decodificador = new ArithDecoder (new BitInput(testedecoder));
			
			System.out.println("Decodificador: " + decodificador.getCurrentSymbolCount(10));
			
			decodificador.removeSymbolFromStream(1, 3, 10);
			
			System.out.println("Decodificador: " + decodificador.getCurrentSymbolCount(13));
			
			decodificador.removeSymbolFromStream(5,9,13);
			
			testedecoder.close();
			decodificador.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
