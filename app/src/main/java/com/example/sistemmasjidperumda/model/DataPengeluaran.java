package com.example.sistemmasjidperumda.model;

public class DataPengeluaran {
    String type_keluar;
    int amount_keluar;
    String note_keluar;
    String date_keluar;
    String id_keluar;

    public DataPengeluaran() {

    }

    public DataPengeluaran(String type, int amount, String note, String date, String id) {
        this.type_keluar = type;
        this.amount_keluar = amount;
        this.note_keluar = note;
        this.date_keluar = date;
        this.id_keluar = id;
    }

    public String getType() {
        return type_keluar;
    }

    public void setType(String type) {
        this.type_keluar = type;
    }

    public int getAmount() {
        return amount_keluar;
    }

    public void setAmount(int amount) {
        this.amount_keluar = amount;
    }

    public String getNote() {
        return note_keluar;
    }

    public void setNote(String note) {
        this.note_keluar = note;
    }

    public String getDate() {
        return date_keluar;
    }

    public void setDate(String date) {
        this.date_keluar = date;
    }

    public String getId() {
        return id_keluar;
    }

    public void setId(String id) {
        this.id_keluar = id;
    }
}
