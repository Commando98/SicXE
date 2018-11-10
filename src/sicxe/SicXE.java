package sicxe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class SicXE {

    public static void main(String[] args) throws FileNotFoundException {
        LinkedList<String> labeles = new LinkedList<>();
        LinkedList<String> instrections = new LinkedList<>();
        LinkedList<String> varbile = new LinkedList<>();
        LinkedList<String> Adress = new LinkedList<>();
        String format = null;
        converter.initialize();
        converter con = new converter();
        File file = new File("/home/ahmed/Desktop/TestXE.txt");

        Scanner sc = new Scanner(file);

        //split 
        while (sc.hasNext()) {
            labeles.add(sc.next());
            instrections.add(sc.next());
            varbile.add(sc.next());
        }
        
        int j, total, curr, prev = 0;
        
        for (j = 0; j < instrections.size(); j++) {
            if (instrections.get(j).equals("START")) {
                Adress.add(varbile.get(j));
                Adress.add(varbile.get(j));
                Adress.add(varbile.get(j));
                total = Integer.parseInt(varbile.get(j), 16);
                prev = total;
            } else if (instrections.get(j).equals("RESW")) {
                curr = 3 * Integer.valueOf(varbile.get(j));
                total = curr + prev;
                Adress.add(Integer.toHexString(total));
                prev = total;
            } else if (instrections.get(j).equals("RESB")) {
                curr = Integer.valueOf(varbile.get(j));
                total = curr + prev;
                Adress.add(Integer.toHexString(total));
                prev = total;
            } else if (instrections.get(j).equals("BYTE")) {
                if (varbile.get(j).charAt(0) == 'X') {
                    curr = (varbile.get(j).length() - 3) / 2;
                    total = curr + prev;
                    Adress.add(Integer.toHexString(total));
                    prev = total;
                } else if (varbile.get(j).charAt(0) == 'C') {
                    curr = varbile.get(j).length() - 3;
                    total = curr + prev;
                    Adress.add(Integer.toHexString(total));
                    prev = total;
                }
            } else if (instrections.get(j).charAt(0) == '+') {
                curr = 4;
                total = curr + prev;
                Adress.add(Integer.toHexString(total));
                prev = total;
            } else if (instrections.get(j).equals("BASE")) {
                curr = 0;
                total = curr + prev;
                Adress.add(Integer.toHexString(total));
                prev = total;
            } else {
                int flag = 0;
                for (int k = 0; k < 59 && flag == 0; k++) {
                    if (instrections.get(j).equals(con.find(k))) {
                        format = con.gettype(k, 1);
                        flag = 1;
                    }
                }
                flag = 0;
                if (format.equals("1")) {
                    curr = 1;
                    total = curr + prev;
                    Adress.add(Integer.toHexString(total));
                    prev = total;
                } else if (format.equals("2")) {
                    curr = 2;
                    total = curr + prev;
                    Adress.add(Integer.toHexString(total));
                    prev = total;
                } else {
                    String[] s = new String[10];
                    s = varbile.get(j).split(",");
                    if (s.length > 1 && instrections.get(j).equals("WORD")) {
                        curr = 3 * s.length;
                        total = curr + prev;
                        Adress.add(Integer.toHexString(total));
                        prev = total;
                    } else {
                        curr = 3;
                        total = curr + prev;
                        Adress.add(Integer.toHexString(total));
                        prev = total;
                    }
                }
            }
        }

        for (int i = 0; i < instrections.size(); i++) {
            System.out.println( Adress.get(i));
        }
    }

}
