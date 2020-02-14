package com.jusco.juscoLogic;

import com.jusco.ConsFetchActivity;
import com.jusco.EntryActivity;
import com.jusco.JuscoTarriff;

public class Cs_Urban {

    JuscoTarriff jt=new JuscoTarriff();

    public Double calc_CSUec(Double Units)
    {
        Double Energy_Charges=0.00;
        Energy_Charges=Units*jt.CSU_EC;
        EntryActivity.cat_tariff =jt.CSU_EC;
        return Energy_Charges;
    }

    public Double calc_CSUfc(Double Load)
    {
        Double fixed_Charges=0.00d;
        if(Load>0.00d)
            fixed_Charges=jt.CSU_FC*Load;
        else
            fixed_Charges=jt.CSU_FC;

        fixed_Charges=Double.parseDouble(ConsFetchActivity.sbmRec.getFixedCharge());

        return fixed_Charges;
    }

    public Double calc_CSUed(Double units)
    {
        Double ed=0.00;

        switch(ConsFetchActivity.sbmRec.getE_Duty_Identifier())
        {
            case "ED24&30PSK": {
                if(units<=250){
                    ed=units*0.24;
                }
                else if(units>250){
                    ed=(250*0.24)+((units-250)*0.30);
                }
            }
            break;
            case "ED@15P-SK": {
                    ed=units*0.15;
            }
            break;
            case "ED@20P-SK": {
                ed=units*0.2;
            }
            break;
            default:
                if(units<=250){
                    ed=units*0.24;
                }
                else if(units>250){
                    ed=(250*0.24)+((units-250)*0.30);
                }

                break;
        }




        return ed;
    }

    public Double calc_fppa(Double units,Double rate)
    {
        Double fppa=0.00d;

        fppa=units*rate;

        return fppa;
    }

}
