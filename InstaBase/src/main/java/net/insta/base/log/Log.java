package net.insta.base.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import net.insta.base.utils.Utils;

public class Log {
	
	public static void init(){
		try {
			File dir = new File(System.getProperty("user.dir")+ File.separator+"logs");
			dir.mkdirs();
			
			File file = new File(dir.getAbsolutePath()+File.separator+"log-"+dir.listFiles().length+"-"+Utils.toDate(System.currentTimeMillis())+".txt");
			if (!file.exists()) {
			    file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			
			PrintStream previous = System.out;   
		    OutputStream outputStreamCombiner =  new OutputStreamCombiner(Arrays.asList(previous, out)); 
		    PrintStream custom = new PrintStream(outputStreamCombiner);
		    System.setOut(custom);
		    System.setErr(custom);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private static class OutputStreamCombiner extends OutputStream {
        private List<OutputStream> outputStreams;

        public OutputStreamCombiner(List<OutputStream> outputStreams) {
            this.outputStreams = outputStreams;
        }

        public void write(int b) throws IOException {
            for (OutputStream os : outputStreams) {
                os.write(b);
            }
        }

        public void flush() throws IOException {
            for (OutputStream os : outputStreams) {
                os.flush();
            }
        }

        public void close() throws IOException {
            for (OutputStream os : outputStreams) {
                os.close();
            }
        }
	}

}