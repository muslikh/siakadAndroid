package id.codemerindu.siakad;

public class ModelData {
    String username,password,nisn,nama,jurusan,thnmasuk;

    public ModelData(){}

    public ModelData(String username, String password, String nisn, String nama, String jurusan, String thnmasuk) {
        this.username = username;
        this.password = password;
        this.nama = nama;
        this.nisn = nisn;
        this.jurusan = jurusan;
        this.thnmasuk = thnmasuk;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public String getJurusan() {
        return nisn;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }
    public String getThnmasuk() {
        return thnmasuk;
    }

    public void setThnmasuk(String thnmasuk) {
        this.thnmasuk = thnmasuk;
    }

}
