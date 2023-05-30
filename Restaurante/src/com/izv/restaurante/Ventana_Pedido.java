package com.izv.restaurante;

	import java.util.Calendar;
	import com.izv.restaurante.db.GestionCarta;
	import com.izv.restaurante.db.GestionDetallePedido;
	import com.izv.restaurante.db.GestionPedido;
	import com.izv.restaurante.pojo.Mesa;
	import com.izv.restaurante.pojo.Pedido;
	import android.app.Activity;
	import android.database.Cursor;
	import android.os.Bundle;
	import android.view.Menu;
	import android.view.MenuInflater;
	import android.view.MenuItem;
	import android.widget.ListView;
	import android.widget.Toast;

public class Ventana_Pedido extends Activity {
	
	Mesa mesa=new Mesa();
	static Pedido pedido=new Pedido();
	GestionPedido gestorPedido;
	GestionCarta gestorCarta;
	GestionDetallePedido gestorDetalle;
	AdaptadorPedido adaptadorPedido;
	int estado=0;
	int idMesa;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ventana_pedido);
		Bundle b=getIntent().getExtras();
		Mesa mesa=(Mesa) b.getSerializable("Mesa");
		gestorPedido=new GestionPedido(this);
		gestorPedido.open();
		idMesa=mesa.getId();
		//Consulta para saber si hay pedidos abiertos
		//Si los hay, los recoge
		//Si no los hay, crea uno nuevo
		String[] ar={idMesa+""};
		Cursor a=gestorPedido.getPedido(ar);
		if(a.getCount()==0){
			pedido=generarPedido(idMesa);
			estado=0;
		}else{
			a.moveToLast();
			pedido.setId(a.getInt(0));
			pedido.setFechaHora(a.getString(1));
			pedido.setMesa(a.getInt(2));
			pedido.setCerrado(a.getInt(3));
			estado=1;
		}
		//Si se ha creado un pedido nuevo, se crea un detallepedido
		//Luego se lanza el Adaptador
		gestorDetalle=new GestionDetallePedido(this);
		gestorDetalle.open();
		if(estado==0){
			gestorCarta=new GestionCarta(this);
			gestorCarta.open();
			Cursor c=gestorCarta.getCursor(null, null);
			c.moveToFirst();
			if(c.getCount()!=0){
				gestorDetalle=new GestionDetallePedido(this);
				gestorDetalle.open();
				int id=pedido.getId();
				do{
					gestorDetalle.insert(
							new com.izv.restaurante.pojo.DetallePedido(id, c.getInt(0), 
									0, c.getFloat(2)));
					c.moveToNext();
				}while(c.isAfterLast()==false);
				String[] argumentos={pedido.getId()+""};
				c=gestorDetalle.getDetalleTotal(argumentos);
				adaptadorPedido=new AdaptadorPedido(this, c);
				lv=(ListView) findViewById(R.id.lvPedido);
				lv.setAdapter(adaptadorPedido);
			}else{
				gestorPedido.delete(pedido.getId());
				Toast.makeText(this, "No hay elementos en la carta", Toast.LENGTH_LONG).show();
				finish();
			}
		}else{
			//Si existe un pedido, existe un detallepedido
			//Se recoge y se lanza el adaptador
			gestorDetalle=new GestionDetallePedido(this);
			gestorDetalle.open();
			String[] argumento={pedido.getId()+""};
			Cursor d=gestorDetalle.getDetalleTotal(argumento);
			adaptadorPedido=new AdaptadorPedido(this, d);
			lv=(ListView) findViewById(R.id.lvPedido);
			lv.setAdapter(adaptadorPedido);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_ventana_pedido, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_cerrar:
				//Coge el pedido actual y lo cierra
				pedido.setCerrado(1);
				gestorPedido.update(pedido);
				finish();
				return true;
			case R.id.action_abrir:
				try{
					//Busca los pedidos cerrados, coge el ultimo y lo abre
					gestorPedido.delete(pedido);
					String[] args={idMesa+""};
					Cursor e=gestorPedido.getPedidos(args);
					e.moveToLast();
					gestorPedido.updateCerrado(new Pedido(e.getInt(0), e.getString(1), e.getInt(2), 0));
					Toast.makeText(this, "Pedido Anterior Recuperado", Toast.LENGTH_LONG).show();
				}catch(Exception e){}
				finish();
				return true;
		}
    	return super.onOptionsItemSelected(item);
    }
	
	public String generarFechaHora(){
		//Genera la fecha y hora acutal
		Calendar cal = Calendar.getInstance();	
		String hora, minutos, agno, mes, dia;
		if(cal.get(Calendar.HOUR_OF_DAY)<10)
			hora="0"+cal.get(Calendar.HOUR_OF_DAY);
		else
			hora=""+cal.get(Calendar.HOUR_OF_DAY);
		if(cal.get(Calendar.MINUTE)<10)
			minutos="0"+cal.get(Calendar.MINUTE);
		else
			minutos=""+cal.get(Calendar.MINUTE);
		agno=""+cal.get(Calendar.YEAR);
		if(cal.get(Calendar.MONTH)+1<10)
			mes="0"+cal.get(Calendar.MONTH)+1;
		else
			mes=""+cal.get(Calendar.MONTH)+1;
		if(cal.get(Calendar.DAY_OF_MONTH)<10)
			dia="0"+cal.get(Calendar.DAY_OF_MONTH);
		else
			dia=""+cal.get(Calendar.DAY_OF_MONTH);
		return agno+mes+dia+hora+minutos;
	}
	
	public Pedido generarPedido(int id){
		//Genera un nuevo pedido
		Long idPedido=gestorPedido.insert(new Pedido(generarFechaHora(), id, 0));
		return gestorPedido.getRow(idPedido);
	}

}