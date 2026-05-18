package Model.Mahasiswa;

public class ModelMahasiswa {
    private Integer id;
    private String nama, nim;
    
    /*
      Membuat getter dan setter untuk mengambil/mengatur informasi mahasiswa.
      Karena sekarang atribut id, nama, dan nim diubah menjadi private, 
      sehingga atribut2 tersebut tidak dapat diakses secara langsung (Prinsip Enkapsulasi)
    */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }
}

