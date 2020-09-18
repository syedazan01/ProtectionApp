package atoz.protection.model;

public class PersonalRecord {

    String recordsName;
    int docResId;

    public PersonalRecord(String recordsName, int docResId) {
        this.recordsName = recordsName;
        this.docResId = docResId;
    }

    public int getDocResId() {
        return docResId;
    }

    public String getRecordsName() {
        return recordsName;
    }
}
