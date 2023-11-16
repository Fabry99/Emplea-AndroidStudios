package sv.edu.catolica.emplea;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adaptador extends RecyclerView.Adapter<adaptador.ViewHolder> {
    private List<listElement>datos;
    private LayoutInflater miInflater;
    private Context contexto;
    public adaptador(List<listElement> itemlist, Context contexto){
        this.miInflater=LayoutInflater.from(contexto);
        this.contexto=contexto;
        this.datos=itemlist;


    }
    public interface OnClickListener {
        void onItemClick(listElement item);
    }
    @Override
    public int getItemCount(){return datos.size();}

    @Override
    public adaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view=miInflater.inflate(R.layout.listelement,null);
        return new adaptador.ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(final adaptador.ViewHolder holder, @SuppressLint("RecyclerView") final int position){
        holder.bindData(datos.get(position));


        Button btnver=holder.itemView.findViewById(R.id.btnVER);
        btnver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listElement elementoSeleccionado = datos.get(position);
                Context context=v.getContext();
                Intent intent=new Intent(context, VerOfertas.class);
                intent.putExtra("Id_oferta",elementoSeleccionado.getId_oferta());
                intent.putExtra("Nombre", elementoSeleccionado.getTitulo());
                intent.putExtra("Empresa", elementoSeleccionado.getEmpresa());
                intent.putExtra("Direccion", elementoSeleccionado.getDireccion());
                intent.putExtra("Publicado", elementoSeleccionado.getPublicado());
                context.startActivity(intent);
            }
        });


    }
    public void setItems (List<listElement> items){datos=items;}
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id,titulo,empresa,direccion,publicado;
        ViewHolder(View itemView){
            super(itemView);
            id=itemView.findViewById(R.id.id_oferta);
            empresa=itemView.findViewById(R.id.empresa);
            titulo=itemView.findViewById(R.id.Titulo);
            direccion=itemView.findViewById(R.id.dire);
            publicado=itemView.findViewById(R.id.publi);
        }
        void bindData(final listElement item){
            id.setText(item.getId_oferta());
            titulo.setText(item.getTitulo());
            empresa.setText(item.getEmpresa());
            direccion.setText(item.getDireccion());
            publicado.setText(item.getPublicado());

        }

    }
}