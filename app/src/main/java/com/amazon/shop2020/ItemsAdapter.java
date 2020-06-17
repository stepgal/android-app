package com.amazon.shop2020;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ravi on 16/11/17.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> implements Filterable
{
    private Context context;
    private List<Items> itemsList;
    private List<Items> itemsListFiltered;
    private ItemsAdapterListener listener;
    private String URL;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title, description, cost;

        public MyViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            cost = view.findViewById(R.id.cost);

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // send selected contact in callback
                    listener.onItemSelected(itemsListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ItemsAdapter(Context context, List<Items> itemsList, ItemsAdapterListener listener)
    {
        this.context = context;
        this.listener = listener;
        this.itemsList = itemsList;
        this.itemsListFiltered = itemsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        final Items item = itemsListFiltered.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.cost.setText(Float.toString(item.getCost()));

    }

    @Override
    public int getItemCount()
    {
        return itemsListFiltered.size();
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charString = charSequence.toString();
                if (charString.isEmpty())
                {
                    itemsListFiltered = itemsList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                final String charString = charSequence.toString();
                final String TAG = ListAdapter.class.getSimpleName();
                if (charString.isEmpty())
                {

                    URL = serviceAPI.getInstance().getAllItems();
                }
                else
                {
                    URL = serviceAPI.getInstance().searchItems(charString);
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            String status = response.get("status").toString();
                            if (status.equalsIgnoreCase("OK"))
                            {
                                List<Items> filteredList = new ArrayList<>();

                                JSONArray array = response.getJSONArray("items");
                                List<Items> items1 = new Gson().fromJson(array.toString(), new TypeToken<List<Items>>()
                                {
                                }.getType());
                                // adding item to items list
                                itemsListFiltered.clear();
                                itemsListFiltered.addAll(items1);

                                for (Items row : itemsListFiltered)
                                {
                                    filteredList.add(row);
                                }

                                itemsListFiltered = filteredList;


                                FilterResults filterResults = new FilterResults();
                                filterResults.values = itemsListFiltered;
                                itemsListFiltered = (ArrayList<Items>) filterResults.values;
                                notifyDataSetChanged();
                            }
                            else
                            {
                                Toast.makeText(context, "Not Items Found!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, "Error: " + error.getMessage());
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                MainActivity.getInstance().addToRequestQueue(request);


                //                itemsListFiltered = (ArrayList<Items>) filterResults.values;
                //                notifyDataSetChanged();
            }
        };
    }

    public interface ItemsAdapterListener
    {
        void onItemSelected(Items contact);
    }
}