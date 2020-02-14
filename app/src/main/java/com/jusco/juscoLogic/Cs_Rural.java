package com.jusco.juscoLogic;

import com.jusco.ConsFetchActivity;
import com.jusco.EntryActivity;
import com.jusco.JuscoTarriff;

public class Cs_Rural {

    JuscoTarriff jt=new JuscoTarriff();

    public Double calc_CSRec(Double Units)
    {
        Double energy_Charges=0.00;
        energy_Charges=Units*jt.CSR_EC;
        EntryActivity.cat_tariff =jt.CSR_EC;
        return energy_Charges;
    }

    public Double calc_CSRfc(Integer pHase)
    {
        Double fixed_Charges=0.00;
        if(pHase>0)
            fixed_Charges=jt.CSR_FC*pHase;
        else
            fixed_Charges=jt.CSR_FC;
        fixed_Charges=Double.parseDouble(ConsFetchActivity.sbmRec.getFixedCharge());
        return fixed_Charges;
    }

    public Double calc_CSRed(Double units)
    {
        Double ed=0.00;

        switch(ConsFetchActivity.sbmRec.getE_Duty_Identifier())
        {
            case "ED@15P-SK":
                ed=units*0.15;
            break;
            default:
                ed=units*0.15;
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
