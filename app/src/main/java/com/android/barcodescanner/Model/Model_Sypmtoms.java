package com.android.barcodescanner.Model;

public class Model_Sypmtoms {
    String id,symtomName,formula, Medicines;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymtomName() {
        return symtomName;
    }

    public void setSymtomName(String symtomName) {
        this.symtomName = symtomName;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getMedicines() {
        return Medicines;
    }

    public void setMedicines(String medicines) {
        Medicines = medicines;
    }

    public Model_Sypmtoms(String id, String symtomName, String formula, String medicines) {
        this.id = id;
        this.symtomName = symtomName;
        this.formula = formula;
        Medicines = medicines;
    }

    public Model_Sypmtoms() {

    }


}
