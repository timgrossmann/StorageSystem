package testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

public class PrintTest {

	public static void main(String[] args) {
		
		File file = new File(System.getProperty("user.home") + "/Desktop/" + "test.txt");
		
		PrintService service = PrintServiceLookup.lookupDefaultPrintService();

		try (FileInputStream in = new FileInputStream(file)) {
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			pras.add(new Copies(1));

			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			Doc doc = new SimpleDoc(in, flavor, null);

			DocPrintJob job = service.createPrintJob();
			job.print(doc, pras);
			
			System.out.println("Done");
		} catch (PrintException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
}