package sv.edu.catolica.emplea;

public class listElement {
    public String titulo,empresa,direccion,publicado,Id_oferta;



    public listElement(String titulo, String empresa, String direccion, String publicado, String Id_oferta) {
        this.titulo = titulo;
        this.empresa = empresa;
        this.direccion=direccion;
        this.publicado=publicado;
        this.Id_oferta=Id_oferta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }



    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPublicado() {
        return publicado;
    }

    public void setPublicado(String publicado) {
        this.publicado = publicado;
    }

    public  void setId_oferta(String Id_oferta){this.Id_oferta=Id_oferta;}
    public  String getId_oferta(){return Id_oferta;}


}
