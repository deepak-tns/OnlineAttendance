package com.india.lhq.onlineattendance.constants;

public class CustomClass {

    public static  interfaceCustom minterfaceCustom;

   public interface  interfaceCustom{
        void setData(String Data);
    }
    public static CustomClass customClass;

    public static CustomClass getCustomclass(){

     if(customClass == null){
         customClass = new CustomClass();
     }
     return customClass;
    }

    public static void setInterfaceCustom(interfaceCustom interfaceCustom){
        minterfaceCustom = interfaceCustom;
    }

    public void setNotifyData(String data){
        if(minterfaceCustom != null) {
            minterfaceCustom.setData(data);
        }
     //  interfaceCustom.setData(data);
    }
}
