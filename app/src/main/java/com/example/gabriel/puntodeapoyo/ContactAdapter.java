package com.example.gabriel.puntodeapoyo;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gabriel.puntodeapoyo.databinding.ItemAlertListBinding;

import java.util.List;

/**
 * Created by gabii on 02/01/2018.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>{
    List<Contact> mContactos;
    private AdapterCallback onClick;

    public ContactAdapter(List<Contact> mContactos) {
            this.mContactos = mContactos;
        }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemAlertListBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_alert_list,parent,false);
        return new ContactHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ContactHolder holder, final int position) {
            holder.bindConnection(mContactos.get(position));
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onClick.onItemClick(position);
                    return false;
                }
            });
    }

    @Override
    public int getItemCount() {
        return mContactos.size();
    }
    public void setOnClick(AdapterCallback onClick){
            this.onClick=onClick;
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        private ItemAlertListBinding mBinding;


        public ContactHolder(ItemAlertListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bindConnection(Contact contact) {
            mBinding.setContact(contact);
        }
    }
}
