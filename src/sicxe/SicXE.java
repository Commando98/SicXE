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
        String [][] objCode = new String[100][2];
        String [] symLocS = new String[100];
        String [] symLocL = new String[100];
        String base=null,pc=null;
        String format = null;
        converter.initialize();
        converter con = new converter();
        File file = new File("/home/x/Desktop/TestXE.txt");

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

        // simple table
        for(int w=0;w<labeles.size();w++){
            if(!instrections.get(w).equals(";")){
                if((!labeles.get(w).equals("START"))){
                    symLocS[w] = labeles.get(w);
                    symLocL[w]= Adress.get(w);
                    System.out.println(symLocS[w]+"\t"+symLocL[w]);
                }
            }


        // get the object code
            int flag;
         for(int i = 0; i< instrections.size();i++){
            if(labeles.get(i).equals("START") ||labeles.get(i).equals("END") || labeles.get(i).equals("RESW") || labeles.get(i).equals("RESB")|| labeles.get(i).equals("BASE")){
                objCode[i][0] = "No";
                objCode[i][1] = " obj. code";
            }else if(labeles.get(i).equals("WORD")){
                if(instrections.get(i).split(",").length>1){
                    String [] s = new String[100];
                    s =instrections.get(i).split(",");
                    for(int q =0;q<s.length;q++){
                        System.out.println("\t\t\t\t"+String.format("%06x",Integer.parseInt(s[q])));
                    }
                }else{
                    objCode[j][0] = "00";
                    objCode[j][1] = String.format("%04x",Integer.parseInt(labeles.get(i)));}
            }else if(labeles.get(i).equals("BYTE")){
                if(instrections.get(i).charAt(0)=='X'){
                    String[] s = new String[3];
                    s = instrections.get(i).split("'");
                    objCode[j][1] = s[1];
                }else if(instrections.get(i).charAt(0)=='C'){
                    objCode[j][1] = Integer.toHexString((int) instrections.get(i).charAt(2));
                    for(int z=3;z<instrections.get(i).length()-1;z++){
                        String last =  Integer.toHexString((int) instrections.get(i).charAt(z));
                        objCode[j][1] = objCode[j][1].concat(last);
                    }
                }
            }else{
                if(varbile.get(i).charAt(0)=='+'){
                    String[] s = new String[2];
                    String s1;
                    s1 = varbile.get(i);
                    s1= s1.replace("+", "-");
                    s = s1.split("-");
                    flag = 0;
                    for(int k=0;k<59 && flag==0;k++){
                        if(s[1].equals(con.find(k))){
                            objCode[j][0] = con.getopcode(k,2);
                            flag = 1;
                        }
                    }
                    flag =0;
                    if(varbile.get(i).charAt(0)=='#'){
                        objCode[j][0] = String.format("%02x",Integer.parseInt(objCode[i][0],16)+1);
                    }else if(labeles.get(i).charAt(0)=='@'){
                        objCode[j][0] = String.format("%02x",Integer.parseInt(objCode[i][0],16)+2);
                    }else{
                        objCode[j][0] = String.format("%02x",Integer.parseInt(objCode[i][0],16)+3);
                    }

                    if(varbile.get(i).contains(",")){
                        s = new String[2];
                        s = varbile.get(i).split(",");
                        objCode[j][1] ="9";
                        for(int k = 0; k<symLocS.length && flag==0;k++){
                            if(s[0].equals(symLocS[k])){
                                objCode[j][1] = objCode[j][1]+String.format("%05x",Integer.parseInt(symLocL[k]));
                                flag = 1;
                            }
                            if(flag==0){
                                objCode[j][1] = objCode[j][1]+String.format("%05x",Integer.parseInt(s[0]));
                            }
                        }
                    }else{
                        if(varbile.get(i).charAt(0)=='#'){
                            flag =0;
                            String[] s9 = new String[2];
                            s9 = varbile.get(i).split("#");
                            objCode[j][1] ="1";
                            for(int k = 0; k<symLocS.length && flag==0;k++){
                                if(s9[1].equals(symLocS[k])){
                                    objCode[j][1] = objCode[j][1]+String.format("%05x",Integer.parseInt(symLocL[k],16));
                                    flag = 1;
                                }
                            }
                            if(flag==0){
                                objCode[j][1] = objCode[j][1]+String.format("%05x",Integer.parseInt(s9[1]));
                            }
                        }else{
                            objCode[j][1] ="1";
                            for(int k = 0; k<symLocS.length && flag==0;k++){
                                if(labeles.get(i).equals(symLocS[k])){
                                    objCode[j][1] = objCode[j][1]+String.format("%05x",Integer.parseInt(symLocL[k],16));
                                    flag = 1;
                                }
                            }
                            if(flag==0){
                                System.out.println("=======>"+labeles.get(i));
                                objCode[j][1] = objCode[j][1]+String.format("%05x",Integer.parseInt(labeles.get(i),16));
                            }
                        }
                    }
                }else{
                    flag = 0;
                    String f=new String();
                    for(int k=0;k<59 && flag==0;k++){
                        if(instrections.get(i).equals(con.find(k))){
                            f=con.getopcode(k,1);
                            objCode[j][0] = con.getopcode(k,2);
                            flag = 1;
                        }
                    }
                    flag = 0;
                    if(f.equals("1")){
                        objCode[j][1] ="  ";
                    }
                    else if(instrections.get(i).equals("RSUB")){
                        objCode[j][0]= String.format("%02x",Integer.parseInt(objCode[j][0],16)+3);
                        objCode[j][1] ="0000";
                    }else if(f.equals("2")){
                        String[] s = new String[2];
                        s = labeles.get(i).split(",");
                        if(s.length>1){
                            for(int k=0;k<10 && flag==0;k++){
                                if(s[0].equals(con.R[k])){
                                    objCode[j][1] = Integer.toString(k);
                                    flag=1;
                                }
                            }
                            flag=0;
                            for(int k=0;k<10 && flag==0;k++){
                                if(s[1].equals(con.R[k])){
                                    objCode[j][1] = objCode[j][1]+Integer.toString(k);
                                    flag=1;
                                }
                            }
                        }else{
                            for(int k=0;k<10 && flag==0;k++){
                                if(s[0].equals(con.R[k])){
                                    objCode[j][1] =Integer.toString(k)+"0";
                                    flag=1;
                                }
                            }
                            flag=0;
                        }
                    }else{
                        if(varbile.get(i).charAt(0)=='#'){
                            objCode[j][0] = String.format("%02x",Integer.parseInt(objCode[j][0],16)+1);
                        }else if(varbile.get(i).charAt(0)=='@'){
                            objCode[j][0] = String.format("%02x",Integer.parseInt(objCode[j][0],16)+2);
                        }
                        if(varbile.get(i).contains(",")){
                            if(labeles.get(i).charAt(0)=='@'){
                                flag =0;
                                String[] s = new String[2];
                                s = varbile.get(i).split("@");
                                s=s[0].split(",");
                                objCode[j][1] ="8";
                                for(int k = 0; k<symLocS.length && flag==0;k++){
                                    if(s[0].equals(symLocS[k])){
                                        int v = Integer.parseInt(symLocL[k],16)-Integer.parseInt(pc,16);
                                        if(v<2047 && v>-2048){
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+2);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",v);
                                        }else{
                                            objCode[j][0]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+4);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",Integer.parseInt(symLocL[k],16)-Integer.parseInt(base,16));
                                        }
                                        flag =1;
                                    }
                                    if(flag==0){
                                        objCode[j][1] = objCode[j][1]+String.format("%03x",Integer.parseInt(s[0]));
                                    }
                                }
                            }else if(varbile.get(i).charAt(0)=='#'){
                                flag =0;
                                String[] s = new String[2];
                                s = varbile.get(i).split("#");
                                s = s[0].split(",");
                                objCode[j][1] ="8";
                                for(int k = 0; k<symLocS.length && flag==0;k++){
                                    if(s[0].equals(symLocS[k])){

                                        int v = Integer.parseInt(symLocL[k],16);
                                        objCode[j][1]=objCode[j][1]+String.format("%03x",v);
                                        flag = 1;
                                    }
                                }
                                if(flag==0){
                                    objCode[j][1] = objCode[j][1]+String.format("%03x",Integer.parseInt(s[0]));
                                }
                            }else{
                                flag =0;
                                String []s = new String[2];
                                s = varbile.get(i).split(",");
                                objCode[j][1] ="8";
                                for(int k = 0; k<symLocS.length && flag==0;k++){
                                    if(s[0].equals(symLocS[k])){
                                        int v = Integer.parseInt(symLocL[k],16)-Integer.parseInt(pc,16);
                                        if(v<2047&&v>-2048){
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+2);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",v);
                                            flag =1;
                                        }else{
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+4);
                                            int v1 = Integer.parseInt(symLocL[k],16) - Integer.parseInt(base,16);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",v1);
                                            flag =1;
                                        }
                                    }
                                }
                                if(flag==0){
                                    objCode[j][1] = objCode[j][1]+String.format("%03x",Integer.parseInt(s[0]));
                                }
                            }
                        }else{
                            if(varbile.get(i).charAt(0)=='@'){
                                flag =0;
                                String[] s = new String[2];
                                s = varbile.get(i).split("@");
                                objCode[j][1] ="0";
                                for(int k = 0; k<symLocS.length && flag==0;k++){
                                    if(s[1].equals(symLocS[k])){
                                        int v = Integer.parseInt(symLocL[k],16)-Integer.parseInt(pc,16);
                                        if(v<2047 && v>-2048){
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+2);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",v);
                                        }else{
                                            objCode[j][0]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+4);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",Integer.parseInt(symLocL[k],16)-Integer.parseInt(base,16));
                                        }
                                        flag =1;
                                    }
                                }
                                if(flag==0){
                                    int v = Integer.parseInt(s[1],16)-Integer.parseInt(pc,16);
                                    if(v<2047&&v>-2048){
                                        objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+2);
                                        objCode[j][1]=objCode[j][1]+String.format("%03x",v);
                                    }else{
                                        objCode[j][0]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+4);
                                        objCode[j][1]=objCode[j][1]+String.format("%03x",Integer.parseInt(s[0],16)-Integer.parseInt(base,16));
                                    }
                                    objCode[j][1] = objCode[j][1]+String.format("%03x",v);
                                }
                            }else if(varbile.get(i).charAt(0)=='#'){
                                flag =0;
                                String[] s = new String[2];
                                s = varbile.get(i).split("#");
                                objCode[j][1] ="2";
                                for(int k = 0; k<symLocS.length && flag==0;k++){
                                    if(s[1].equals(symLocS[k])){
                                        int v = Integer.parseInt(symLocL[k],16)-Integer.parseInt(pc,16);
                                        objCode[j][1]=objCode[j][1]+String.format("%03x",v);
                                        flag = 1;
                                    }
                                }
                                if(flag==0){
                                    objCode[j][1] ="0";
                                    objCode[j][1] = objCode[j][1]+String.format("%03x",Integer.parseInt(s[1]));
                                }
                            }else{
                                String []s = new String[2];
                                objCode[j][1] ="0";
                                for(int k = 0; k<symLocS.length && flag==0;k++){
                                    if(labeles.get(i).equals(symLocS[k])){
                                        int v = Integer.parseInt(symLocL[k],16)-Integer.parseInt(pc,16);
                                        if(v<2047&&v>0){
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+2);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",v);
                                        }else if(v>-2048&& v<0){
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+2);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",v).substring(String.format("%03x",v).length() - 3);
                                        }
                                        else{
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+4);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",Integer.parseInt(symLocL[k],16)-Integer.parseInt(base,16));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        for (int i = 0; i < instrections.size(); i++) {
            System.out.println("adress is " +Adress.get(i) + "label is "+ labeles.get(i)+ "instructions is"+instrections.get(i)+"varible is "+varbile.get(i));
        }
    }

}
}
