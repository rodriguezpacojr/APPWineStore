package com.rodriguezpacojr.winestore.adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rodriguezpacojr.winestore.R;
import com.rodriguezpacojr.winestore.Setup;
import com.rodriguezpacojr.winestore.admin.CRUDCustomer;
import com.rodriguezpacojr.winestore.admin.CRUDEmployee;
import com.rodriguezpacojr.winestore.models.Person;

import java.util.List;

/**
 * Created by francisco on 4/21/18.
 */

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.PersonsHolder> {

    private List<Person> personsList;
    Context context;
    public static int key, keyRoute;
    public static String name, lastName, bornDate, phone, email, entryDate, rfc;
    public static Double latitude, longitude;
    public static boolean flagUpdate = false;

    public static class PersonsHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName;
        TextView tvlastName;
        TextView tvbornDate;
        TextView tvrfc;

        ImageView btnCall;
        ImageView btnMail;
        ImageView btnSms;
        ImageView btnDelete;
        ImageView btnEdit;

        public PersonsHolder(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvlastName = itemView.findViewById(R.id.tvlastName);
            tvbornDate = itemView.findViewById(R.id.tvbornDate);
            tvrfc = itemView.findViewById(R.id.tvrfc);

            btnCall = itemView.findViewById(R.id.btnCall);
            btnMail = itemView.findViewById(R.id.btnMail);
            btnSms = itemView.findViewById(R.id.btnSms);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }

    public CustomerListAdapter(List<Person> List, Context con) {
        personsList = List;
        context = con;
    }

    @Override
    public PersonsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_row, parent, false);
        CustomerListAdapter.PersonsHolder PersonsHolder = new CustomerListAdapter.PersonsHolder(view);
        return PersonsHolder;
    }

    @Override
    public void onBindViewHolder(PersonsHolder holder, int position) {
        final Person person= personsList.get(position);

        holder.tvName.setText(person.getName());
        holder.tvlastName.setText(person.getLastName());
        holder.tvbornDate.setText((CharSequence) person.getBornDate());
        holder.tvrfc.setText(person.getRfc());

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent objInt = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+person.getPhone()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    return;
                context.startActivity(objInt);
            }
        });

        holder.btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(Intent.ACTION_SEND);
                obj.setType("text/plain");
                obj.putExtra(obj.EXTRA_SUBJECT, "My first email");
                obj.putExtra(obj.EXTRA_TEXT, "Hello, this is my first email on Android");
                obj.putExtra(obj.EXTRA_EMAIL, new String[]{person.getEmail()});

                context.startActivity(obj);
            }
        });

        holder.btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intSms = new Intent(Intent.ACTION_SENDTO);
                intSms.setData(Uri.parse("sms: "+person.getPhone()));
                intSms.putExtra("sms_body","");
                context.startActivity(intSms);
        }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Delete")
                        .setMessage("You will delete "+person.getName()+" "+person.getLastName()+".\nAre you Sure?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                                CRUDCustomer objC = new CRUDCustomer();
                                objC.deleteCustomer(person.getKeyPerson());
                                Toast.makeText(context, "The Customer was deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            }
                        });
                dialog.show();
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagUpdate = true;

                key = person.getKeyPerson();
                name = person.getName();
                lastName = person.getLastName();
                bornDate = person.getBornDate();
                phone= person.getPhone();
                email = person.getEmail();
                rfc = person.getRfc();
                entryDate = person.getEntryDate();
                latitude = person.getLatitude();
                longitude = person.getLongitude();
                keyRoute = person.getKeyRoute();

                Intent intent= new Intent(context, CRUDCustomer.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return personsList.size();
    }
}