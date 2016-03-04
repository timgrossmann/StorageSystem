package save_load;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gui.Controller;

public class Printing {

	private static Logger log = LogManager.getLogger(Controller.class);

	private Printing() {
	}

	/**
	 * Prints the file with the currently set Default Printer of the Device
	 * @param file
	 * @return
	 */
	public static boolean printFile(File file) {
		String defaultPrinter = PrintServiceLookup.lookupDefaultPrintService().getName();
		log.debug("Default printer: " + defaultPrinter);

		//gets the default Printer
		PrintService service = PrintServiceLookup.lookupDefaultPrintService();

		try (FileInputStream in = new FileInputStream(file)) {
			PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
			//only 1 Copy of the selected File
			pras.add(new Copies(1));

			//defines that the input is a filestream from a file
			DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
			Doc doc = new SimpleDoc(in, flavor, null);

			//creates the job and starts printing
			DocPrintJob job = service.createPrintJob();
			job.print(doc, pras);

			return true;
		} catch (PrintException e) {
			log.error("PrintException: " + e.getMessage());
		} catch (IOException e) {
			log.error("IOException: " + e.getMessage());
		}

		return false;
	}

}
