package com.jusco.juscoLogic;

import com.jusco.ConsFetchActivity;
import com.jusco.EntryActivity;
import com.jusco.JuscoTarriff;

public class Ds_Rural {

    JuscoTarriff jt=new JuscoTarriff();

    public Double calc_DSRec(Double Units)
    {
        Double energy_Charges=0.00;
        energy_Charges=Units*jt.DSR_EC;
        EntryActivity.cat_tariff =jt.DSR_EC;
        return energy_Charges;
    }

    public Double calc_DSRfc(Integer pHase)
    {
        Double fixed_Charges=0.00;
        if(pHase>0)
            fixed_Charges=jt.DSR_FC*pHase;
        else
            fixed_Charges=jt.DSR_FC;

        fixed_Charges=Double.parseDouble(ConsFetchActivity.sbmRec.getFixedCharge());
        return fixed_Charges;
    }
    public Double calc_DSRed(Double units)
    {
        Double ed=0.00;

        switch(ConsFetchActivity.sbmRec.getE_Duty_Identifier())
        {
            case "ED20&24PSK": {
                if (units <= 250) {
                    ed = units * 0.20;
                } else if (units > 250) {
                    ed = (250 * 0.2) + ((units - 250) * 0.24);
                }
            }
                break;
            case "ED@15P-SK":
                ed = units * 0.15;
                break;
            default:
                if (units <= 250) {
                    ed = units * 0.20;
                } else if (units > 250) {
                    ed = (250 * 0.2) + ((units - 250) * 0.24);
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
