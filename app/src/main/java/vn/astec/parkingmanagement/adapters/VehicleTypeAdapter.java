package vn.astec.parkingmanagement.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.astec.parkingmanagement.R;
import vn.astec.parkingmanagement.models.VehicleType;

public class VehicleTypeAdapter extends RecyclerView.Adapter<VehicleTypeAdapter.VehicleTypeHolder> {

    private List<VehicleType> vehicleTypes;
    private boolean enableClick = false;

    public interface IOnclickListener {
        void onClick(int position);
    }

    private Context context;

    private IOnclickListener iOnclickListener;


    public VehicleTypeAdapter(Context context, List<VehicleType> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
        this.context = context;
    }

    @NonNull
    @Override
    public VehicleTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_type_item, parent, false);
        return new VehicleTypeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleTypeHolder holder, int position) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int widthOneItem = (int) ((double) width / 4);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthOneItem, widthOneItem);
        params.setMarginStart((int)((double)widthOneItem/6));
        params.setMarginEnd((int)((double)widthOneItem/6));
        holder.layout.setLayoutParams(params);
        holder.imgIcon.setImageResource(vehicleTypes.get(position).getIcon());
        holder.imgIcon.setColorFilter(ContextCompat.getColor(context, vehicleTypes.get(position).isCheck() ? R.color.white : R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY);
        holder.imgCheck.setVisibility(vehicleTypes.get(position).isCheck() ? View.VISIBLE : View.GONE);
        holder.imgCheck.setColorFilter(ContextCompat.getColor(context, vehicleTypes.get(position).isCheck() ? R.color.white : R.color.teal_200), android.graphics.PorterDuff.Mode.MULTIPLY);
        holder.layout.setBackground(ContextCompat.getDrawable(context, vehicleTypes.get(position).isCheck() ? R.drawable.bg_layout_rv_list_checked : R.drawable.bg_layout_rv_list_not_check));
        holder.tvVehicleType.setText(vehicleTypes.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enableClick){
                    selectItem(position);
                }
            }
        });
    }

    public void selectItem(int position) {
        for (int i = 0; i < vehicleTypes.size(); i++) {
            if (i == position) {
                vehicleTypes.get(i).setCheck(true);
            } else {
                vehicleTypes.get(i).setCheck(false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return vehicleTypes.size();
    }

    public class VehicleTypeHolder extends RecyclerView.ViewHolder {
        private ImageView imgIcon, imgCheck;
        private TextView tvVehicleType;
        private FrameLayout layout;

        public VehicleTypeHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_icon);
            imgCheck = itemView.findViewById(R.id.img_check);
            tvVehicleType = itemView.findViewById(R.id.tv_vehicle_type_name);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
