    package com.example.myproject;

    import static android.content.Context.MODE_PRIVATE;

    import android.annotation.SuppressLint;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.RadioButton;
    import android.widget.RadioGroup;

    import androidx.annotation.NonNull;
    import androidx.fragment.app.DialogFragment;

    public class SortAppointmentDialog extends DialogFragment {
        RadioGroup Sortby,Order;
        RadioButton Name,Price,Discount,Date,Asc,Desc;
        Button Done,Cancel;
        @SuppressLint("MissingInflatedId")
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.sort_appoi_dialog, container);
            Sortby=view.findViewById(R.id.RGASortby);
            Order=view.findViewById(R.id.RGASOrder);

            Name=view.findViewById(R.id.RBAName);
            Price=view.findViewById(R.id.RBAPrice);
            Discount=view.findViewById(R.id.RBADiscount);
            Date=view.findViewById(R.id.RBADate);
            Asc=view.findViewById(R.id.RBAAsc);
            Desc=view.findViewById(R.id.RBADesc);
            Done=view.findViewById(R.id.BADone);
            Cancel=view.findViewById(R.id.BACancel);


            initRefresh();
            initDoneButton();
            initCancelButton();
            return view;
        }
        public void initRefresh(){
            String sortField = getContext().getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                    .getString("ASortby", "a.Date_time");
            String sortOrder = getContext().getSharedPreferences("SortAndFilter", MODE_PRIVATE)
                    .getString("ASortOrder", "Asc");

            switch (sortField){
                case "p.name":
                    Name.setChecked(true);
                    break;
                case "a.price":
                    Price.setChecked(true);
                    break;
                case "a.discount":
                    Discount.setChecked(true);
                    break;
                case "a.Date_time":
                    Date.setChecked(true);
                    break;
            }
            if(sortOrder.equalsIgnoreCase("Desc")){
                Desc.setChecked(true);
            }else{
                Asc.setChecked(true);
            }
        }
        public void initDoneButton(){
            Done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Name.isChecked()){
                        getContext().getSharedPreferences("SortAndFilter",
                                        MODE_PRIVATE).edit()
                                .putString("ASortby","p.name").apply();
                    }else if (Discount.isChecked()){
                        getContext().getSharedPreferences("SortAndFilter",
                                        MODE_PRIVATE).edit()
                                .putString("ASortby","a.discount").apply();
                    }else if (Date.isChecked()){
                        getContext().getSharedPreferences("SortAndFilter",
                                        MODE_PRIVATE).edit()
                                .putString("ASortby","a.Date_time").apply();
                    }else{
                        getContext().getSharedPreferences("SortAndFilter",
                                        MODE_PRIVATE).edit()
                                .putString("ASortby","a.price").apply();
                    }

                    if (Asc.isChecked()){
                        getContext().getSharedPreferences("SortAndFilter",
                                        MODE_PRIVATE).edit()
                                .putString("ASortOrder","Asc").apply();
                    }else{
                        getContext().getSharedPreferences("SortAndFilter",
                                        MODE_PRIVATE).edit()
                                .putString("ASortOrder","Desc").apply();
                    }
                    SortAppointmentDialog.SortAndFilterListener activity= (SortAppointmentDialog.SortAndFilterListener) getActivity();
                    activity.didFinishSortAndFilterDialog();
                    getDialog().dismiss();
                }
            });
        }
        public void initCancelButton(){
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });

        }


        public interface SortAndFilterListener {
            void didFinishSortAndFilterDialog();
        }

    }
