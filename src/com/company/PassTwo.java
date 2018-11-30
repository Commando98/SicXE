package com.company;

public class PassTwo {
    PassOne p1;
    String [][] objCode = new String[100][2];
    String [] TRecord = new String[100];
    String pc,base;
    public PassTwo(PassOne p){
        p1=p;
    }

    public void GenerateOPC(){
        int flag = 0;
        for(int j=0;j<p1.i;j++){
            pc = p1.loc[j+1];
            if(p1.ins[j].equals("BASE")){
                flag =0;
                for(int k = 0; k<p1.symLocS.length && flag==0;k++){
                    if(p1.lab[j].equals(p1.symLocS[k])){
                        base = p1.symLocL[k];
                        flag = 1;
                    }
                }
                if(flag==0){
                    String []s = new String[2];
                    s = p1.lab[j].split("#");
                    base = s[1];
                }
                flag = 0;
            }
            if(p1.ins[j].equals("START")||p1.ins[j].equals("END")||p1.ins[j].equals("RESW")||p1.ins[j].equals("RESB")||p1.ins[j].equals("BASE")){
                objCode[j][0] = "No";
                objCode[j][1] = " obj. code";
            }else if(p1.ins[j].equals("WORD")){
                if(p1.lab[j].split(",").length>1){
                    String [] s = new String[100];
                    s = p1.lab[j].split(",");
                    for(int i =0;i<s.length;i++){
                        System.out.println("\t\t\t\t"+String.format("%06x",Integer.parseInt(s[i])));
                    }
                }else{
                    objCode[j][0] = "00";
                    objCode[j][1] = String.format("%04x",Integer.parseInt(p1.lab[j]));}
            }else if(p1.ins[j].equals("BYTE")){
                if(p1.lab[j].charAt(0)=='X'){
                    String[] s = new String[3];
                    s = p1.lab[j].split("'");
                    objCode[j][1] = s[1];
                }else if(p1.lab[j].charAt(0)=='C'){
                    objCode[j][1] = Integer.toHexString((int) p1.lab[j].charAt(2));
                    for(int z=3;z<p1.lab[j].length()-1;z++){
                        String last =  Integer.toHexString((int) p1.lab[j].charAt(z));
                        objCode[j][1] = objCode[j][1].concat(last);
                    }
                }
            }else{
                if(p1.ins[j].charAt(0)=='+'){
                    String[] s = new String[2];
                    String s1;
                    s1 = p1.ins[j];
                    s1= s1.replace("+", "-");
                    s = s1.split("-");
                    flag = 0;
                    for(int k=0;k<59 && flag==0;k++){
                        if(s[1].equals(Converter.OPTAB[k][0])){
                            objCode[j][0] = Converter.OPTAB[k][2];
                            flag = 1;
                        }
                    }
                    flag =0;
                    if(p1.lab[j].charAt(0)=='#'){
                        objCode[j][0] = String.format("%02x",Integer.parseInt(objCode[j][0],16)+1);
                    }else if(p1.lab[j].charAt(0)=='@'){
                        objCode[j][0] = String.format("%02x",Integer.parseInt(objCode[j][0],16)+2);
                    }else{
                        objCode[j][0] = String.format("%02x",Integer.parseInt(objCode[j][0],16)+3);
                    }

                    if(p1.lab[j].charAt(p1.lab[j].length()-2)==',' && p1.lab[j].charAt(p1.lab[j].length()-1)=='X'){
                        s = new String[2];
                        s = p1.lab[j].split(",");
                        objCode[j][1] ="9";
                        for(int k = 0; k<p1.symLocS.length && flag==0;k++){
                            if(s[0].equals(p1.symLocS[k])){
                                objCode[j][1] = objCode[j][1]+String.format("%05x",Integer.parseInt(p1.symLocL[k]));
                                flag = 1;
                            }
                            if(flag==0){
                                objCode[j][1] = objCode[j][1]+String.format("%05x",Integer.parseInt(s[0]));
                            }
                        }
                    }else{
                        if(p1.lab[j].charAt(0)=='#'){
                            flag =0;
                            String[] s9 = new String[2];
                            s9 = p1.lab[j].split("#");
                            objCode[j][1] ="1";
                            for(int k = 0; k<p1.symLocS.length && flag==0;k++){
                                if(s9[1].equals(p1.symLocS[k])){
                                    objCode[j][1] = objCode[j][1]+String.format("%05x",Integer.parseInt(p1.symLocL[k],16));
                                    flag = 1;
                                }
                            }
                            if(flag==0){
                                objCode[j][1] = objCode[j][1]+String.format("%05x",Integer.parseInt(s9[1]));
                            }
                        }else{
                            objCode[j][1] ="1";
                            for(int k = 0; k<p1.symLocS.length && flag==0;k++){
                                if(p1.lab[j].equals(p1.symLocS[k])){
                                    objCode[j][1] = objCode[j][1]+String.format("%05x",Integer.parseInt(p1.symLocL[k],16));
                                    flag = 1;
                                }
                            }
                            if(flag==0){
                                System.out.println("=======>"+p1.lab[j]);
                                objCode[j][1] = objCode[j][1]+String.format("%05x",Integer.parseInt(p1.lab[j],16));
                            }
                        }
                    }
                }else{
                    flag = 0;
                    String f=new String();
                    for(int k=0;k<59 && flag==0;k++){
                        if(p1.ins[j].equals(Converter.OPTAB[k][0])){
                            f=Converter.OPTAB[k][1];
                            objCode[j][0] = Converter.OPTAB[k][2];
                            flag = 1;
                        }
                    }
                    flag = 0;
                    if(f.equals("1")){
                        objCode[j][1] ="  ";
                    }
                    else if(p1.ins[j].equals("RSUB")){
                        objCode[j][0]= String.format("%02x",Integer.parseInt(objCode[j][0],16)+3);
                        objCode[j][1] ="0000";
                    }else if(f.equals("2")){
                        String[] s = new String[2];
                        s = p1.lab[j].split(",");
                        if(s.length>1){
                            for(int k=0;k<10 && flag==0;k++){
                                if(s[0].equals(Converter.R[k])){
                                    objCode[j][1] = Integer.toString(k);
                                    flag=1;
                                }
                            }
                            flag=0;
                            for(int k=0;k<10 && flag==0;k++){
                                if(s[1].equals(Converter.R[k])){
                                    objCode[j][1] = objCode[j][1]+Integer.toString(k);
                                    flag=1;
                                }
                            }
                        }else{
                            for(int k=0;k<10 && flag==0;k++){
                                if(s[0].equals(Converter.R[k])){
                                    objCode[j][1] =Integer.toString(k)+"0";
                                    flag=1;
                                }
                            }
                            flag=0;
                        }
                    }else{
                        if(p1.lab[j].charAt(0)=='#'){
                            objCode[j][0] = String.format("%02x",Integer.parseInt(objCode[j][0],16)+1);
                        }else if(p1.lab[j].charAt(0)=='@'){
                            objCode[j][0] = String.format("%02x",Integer.parseInt(objCode[j][0],16)+2);
                        }else{
                            objCode[j][0] = String.format("%02x",Integer.parseInt(objCode[j][0],16)+3);
                        }
                        if(p1.lab[j].charAt(p1.lab[j].length()-2)==',' && p1.lab[j].charAt(p1.lab[j].length()-1)=='X'){
                            if(p1.lab[j].charAt(0)=='@'){
                                flag =0;
                                String[] s = new String[2];
                                s = p1.lab[j].split("@");
                                s=s[0].split(",");
                                objCode[j][1] ="8";
                                for(int k = 0; k<p1.symLocS.length && flag==0;k++){
                                    if(s[0].equals(p1.symLocS[k])){
                                        int v = Integer.parseInt(p1.symLocL[k],16)-Integer.parseInt(pc,16);
                                        if(v<2047 && v>-2048){
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+2);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",v);
                                        }else{
                                            objCode[j][0]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+4);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",Integer.parseInt(p1.symLocL[k],16)-Integer.parseInt(base,16));
                                        }
                                        flag =1;
                                    }
                                    if(flag==0){
                                        objCode[j][1] = objCode[j][1]+String.format("%03x",Integer.parseInt(s[0]));
                                    }
                                }
                            }else if(p1.lab[j].charAt(0)=='#'){
                                flag =0;
                                String[] s = new String[2];
                                s = p1.lab[j].split("#");
                                s = s[0].split(",");
                                objCode[j][1] ="8";
                                for(int k = 0; k<p1.symLocS.length && flag==0;k++){
                                    if(s[0].equals(p1.symLocS[k])){

                                        int v = Integer.parseInt(p1.symLocL[k],16);
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
                                s = p1.lab[j].split(",");
                                objCode[j][1] ="8";
                                for(int k = 0; k<p1.symLocS.length && flag==0;k++){
                                    if(s[0].equals(p1.symLocS[k])){
                                        int v = Integer.parseInt(p1.symLocL[k],16)-Integer.parseInt(pc,16);
                                        if(v<2047&&v>-2048){
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+2);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",v);
                                            flag =1;
                                        }else{
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+4);
                                            int v1 = Integer.parseInt(p1.symLocL[k],16) - Integer.parseInt(base,16);
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
                            if(p1.lab[j].charAt(0)=='@'){
                                flag =0;
                                String[] s = new String[2];
                                s = p1.lab[j].split("@");
                                objCode[j][1] ="0";
                                for(int k = 0; k<p1.symLocS.length && flag==0;k++){
                                    if(s[1].equals(p1.symLocS[k])){
                                        int v = Integer.parseInt(p1.symLocL[k],16)-Integer.parseInt(pc,16);
                                        if(v<2047 && v>-2048){
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+2);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",v);
                                        }else{
                                            objCode[j][0]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+4);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",Integer.parseInt(p1.symLocL[k],16)-Integer.parseInt(base,16));
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
                            }else if(p1.lab[j].charAt(0)=='#'){
                                flag =0;
                                String[] s = new String[2];
                                s = p1.lab[j].split("#");
                                objCode[j][1] ="2";
                                for(int k = 0; k<p1.symLocS.length && flag==0;k++){
                                    if(s[1].equals(p1.symLocS[k])){
                                        int v = Integer.parseInt(p1.symLocL[k],16)-Integer.parseInt(pc,16);
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
                                for(int k = 0; k<p1.symLocS.length && flag==0;k++){
                                    if(p1.lab[j].equals(p1.symLocS[k])){
                                        int v = Integer.parseInt(p1.symLocL[k],16)-Integer.parseInt(pc,16);
                                        if(v<2047&&v>0){
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+2);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",v);
                                        }else if(v>-2048&& v<0){
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+2);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",v).substring(String.format("%03x",v).length() - 3);
                                        }
                                        else{
                                            objCode[j][1]=Integer.toHexString(Integer.parseInt(objCode[j][1],16)+4);
                                            objCode[j][1]=objCode[j][1]+String.format("%03x",Integer.parseInt(p1.symLocL[k],16)-Integer.parseInt(base,16));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(objCode[j][0]== null){
                if(p1.lab[j].charAt(0)=='C'){
                    System.out.println(p1.loc[j]+"\t"+p1.sym[j]+"\t"+p1.ins[j]+"\t"+p1.lab[j]+"\t\t"+objCode[j][1]);
                }else if(p1.lab[j].charAt(0)=='X'){
                    System.out.println(p1.loc[j]+"\t"+p1.sym[j]+"\t"+p1.ins[j]+"\t"+p1.lab[j]+"\t\t"+objCode[j][1]);
                }
            }else{
                if(p1.lab[j].length()-2>0&&p1.lab[j].charAt(p1.lab[j].length()-2)==',' && p1.lab[j].charAt(p1.lab[j].length()-1)=='X'){
                    System.out.println(p1.loc[j]+"\t"+p1.sym[j]+"\t"+p1.ins[j]+"\t"+p1.lab[j]+"\t"+objCode[j][0] + objCode[j][1]);
                }else{
                    System.out.println(p1.loc[j]+"\t"+p1.sym[j]+"\t"+p1.ins[j]+"\t"+p1.lab[j]+"\t\t"+objCode[j][0] + objCode[j][1]);
                }
            }
        }
    }

    public void HTE(){
        System.out.println("H^"+p1.sym[0]+"^"+String.format("%06x",Integer.parseInt(p1.loc[0],16))+"^"+String.format("%06x",Integer.parseInt(p1.loc[p1.i-1],16)-Integer.parseInt(p1.loc[0],16)));
        String TCode;
        String start = null,end = null;
        int n = 0;
        int flag = 0;
        TCode="";
        for(int i =0; i<p1.i;i++){
            if(objCode[i][0]==null && n!=10){
                if(n==0){
                    start = p1.loc[i];
                }
                TCode = TCode+"^"+objCode[i][1];
                n++;
            }else if(objCode[i][0]!=null && !objCode[i][0].equals("No") && n!=10){
                if(n==0){
                    start = p1.loc[i];
                }
                TCode = TCode+"^"+objCode[i][0]+objCode[i][1];
                n++;

            }else if(objCode[i][0]!=null && objCode[i][0].equals("No")&& !TCode.equals("")){
                if(!p1.ins[i].equals("BASE")){
                    end = p1.loc[i];
                    System.out.println("T^"+String.format("%06x",Integer.parseInt(start,16))+"^"+String.format("%02x",Integer.parseInt(end,16)-Integer.parseInt(start,16))+TCode);
                    TCode="";
                    n=0;
                    //flag = 1;
                }
            }else if(n==10 && flag == 0 && !TCode.equals("")){
                end = p1.loc[i];
                System.out.println("T^"+String.format("%06x",Integer.parseInt(start,16))+"^"+String.format("%02x",Integer.parseInt(end,16)-Integer.parseInt(start,16))+TCode);
                TCode="";
                i--;
                //flag = 1;
                n=0;
            }
        }
        for(int i =0; i<p1.i;i++){
            if(p1.ins[i].charAt(0)=='+'&& p1.lab[i].charAt(0)!='#'){
                System.out.println("M^"+String.format("%06x",Integer.parseInt(p1.loc[i],16)+1)+"^05");
            }
        }
        System.out.println("E."+String.format("%06x",Integer.parseInt(p1.loc[0],16)));
        System.out.println("\n================================================================================");
    }
}
