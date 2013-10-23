package ru.zulyaev.ifmo.lesson6;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Никита
 */
public class ArrayListAdapter<T extends ArrayListAdapter.Item> extends BaseAdapter {
    private List<T> items = new ArrayList<T>();
    private int typeCount = 1;

    public void add(T item) {
        items.add(item);
        typeCount = Math.max(typeCount, item.type + 1);
        notifyDataSetChanged();
    }

    public void add(int position, T item) {
        items.add(position, item);
        typeCount = Math.max(typeCount, item.type + 1);
        notifyDataSetChanged();
    }

    public boolean remove(Item item) {
        if (items.remove(item)) {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public void addAll(Collection<? extends T> collection) {
        items.addAll(collection);
        for (Item item : collection) {
            typeCount = Math.max(typeCount, item.type);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public int getViewTypeCount() {
        return typeCount;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return items.get(position).composeView(convertView, parent);
    }

    public abstract static class Item {
        protected final int type;

        protected Item(int type) {
            this.type = type;
        }

        protected Item() {
            this(0);
        }

        protected int getId() {
            return 0;
        }

        protected abstract View composeView(View convertView, ViewGroup parent);
    }
}
