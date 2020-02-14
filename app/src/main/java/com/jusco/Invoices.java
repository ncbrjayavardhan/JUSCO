package com.jusco;

import java.util.List;

public class Invoices {

    public InvoiceData[] getInvoices(List<String[]> records) {



        InvoiceData[] data = new InvoiceData[records.size()];

        /*for(int i = 0; i < records.size(); i ++) {
            InvoiceData row = new InvoiceData();
            row.id = (i+1);
            row.BPNO = String.valueOf(row.id);
            row.customerName =  "Thomas John Beckett";
            row.customerAddress = "1112, Hash Avenue, NYC";
            row.Mobno="9652800744";
            data[i] = row;
        }*/
int i=0;
        for (String[] name : records) {
            InvoiceData row = new InvoiceData();
            row.BPNO =name[0] ;
            row.customerName =  name[1];
            row.customerAddress = "";
            row.Mobno=name[2];
            data[i] = row;
            i++;

        }
        return data;

    }
}
