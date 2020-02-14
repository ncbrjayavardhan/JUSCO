package com.jusco.juscoLogic;

import com.jusco.ConsFetchActivity;
import com.jusco.EntryActivity;
import com.jusco.JuscoTarriff;

public class Ds_HT {

    JuscoTarriff jt=new JuscoTarriff();

    public Double calc_DSHTec(Double Units)
    {
        Double Energy_Charges=0.00;
        Energy_Charges=Units*jt.DSHT_EC;
        EntryActivity.cat_tariff =jt.DSHT_EC;
        return Energy_Charges;
    }

    public Double calc_DSHTfc(Double Load)
    {
        Double Fixed_Charges=0.00;
        Fixed_Charges=Load*jt.DSHT_FC;
        Fixed_Charges=Double.parseDouble(ConsFetchActivity.sbmRec.getFixedCharge());
        return Fixed_Charges;
    }

    public Double calc_DSHTed(Double units)
    {
        Double ed=0.00;

        if(units<=250){
            ed=units*0.20;
        }
        else if(units>250){
            ed=(250*0.2)+((units-250)*0.24);
        }
        return ed;
    }
}
