package com.phone.plantscollect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<listItem> arrayList = new ArrayList<>();


    public ListAdapter(Context mContext) {
        this.mContext = mContext;
        arrayList.add(new listItem("미상_undefined", "/data1/LJH/plants/", R.drawable.undefined));
        arrayList.add(new listItem("갈퀴나물_viciaamoena", "/data1/LJH/plants/", R.drawable.viciaamoena));
        arrayList.add(new listItem("강아지풀_setariaviridis", "/data1/LJH/plants/", R.drawable.setariaviridis));
        arrayList.add(new listItem("개망초_erigeronannuus", "/data1/LJH/plants/", R.drawable.erigeronannuus));
        arrayList.add(new listItem("개미취_aster", "/data1/LJH/plants/", R.drawable.aster));
        arrayList.add(new listItem("금계국_coreopsis", "/data1/LJH/plants/", R.drawable.coreopsis));
        arrayList.add(new listItem("금창초_ajugadecumbens", "/data1/LJH/plants/", R.drawable.ajugadecumbens));
        arrayList.add(new listItem("꿩의밥_luzulacapitata", "/data1/LJH/plants/", R.drawable.luzulacapitata));
        arrayList.add(new listItem("남천_berberisl", "/data1/LJH/plants/", R.drawable.berberisl));
        arrayList.add(new listItem("달개비_commelinacommunis", "/data1/LJH/plants/", R.drawable.commelinacommunis));
        arrayList.add(new listItem("달맞이꽃_oenothera", "/data1/LJH/plants/", R.drawable.oenothera));
        arrayList.add(new listItem("띠_imperatacylindrica", "/data1/LJH/plants/", R.drawable.imperatacylindrica));
        arrayList.add(new listItem("망초_conyzacanadensis", "/data1/LJH/plants/", R.drawable.conyzacanadensis));
        arrayList.add(new listItem("무릇꽃_muleuskkoch", "/data1/LJH/plants/", R.drawable.muleuskkoch));
        arrayList.add(new listItem("미국쑥부쟁이_symphyotrichumpilosum", "/data1/LJH/plants/", R.drawable.symphyotrichumpilosum));
        arrayList.add(new listItem("민들레_taraxacumplatycarpum", "/data1/LJH/plants/", R.drawable.taraxacumplatycarpum));
        arrayList.add(new listItem("바랭이_digitariaciliaris", "/data1/LJH/plants/", R.drawable.digitariaciliaris));
        arrayList.add(new listItem("벌개미취_asterkoraiensis", "/data1/LJH/plants/", R.drawable.asterkoraiensis));
        arrayList.add(new listItem("비비추_hostalongipes", "/data1/LJH/plants/", R.drawable.hostalongipes));
        arrayList.add(new listItem("살갈퀴_viciaangustifoliavarsegetilis", "/data1/LJH/plants/", R.drawable.viciaangustifoliavarsegetilis));
        arrayList.add(new listItem("선씀바귀_ixerisstrigosa", "/data1/LJH/plants/", R.drawable.ixerisstrigosa));
        arrayList.add(new listItem("소리쟁이_rumexcrispus", "/data1/LJH/plants/", R.drawable.rumexcrispus));
        arrayList.add(new listItem("쇠뜨기_equisetumarvense", "/data1/LJH/plants/", R.drawable.equisetumarvense));
        arrayList.add(new listItem("쇠무릎_achyranthesbidentata", "/data1/LJH/plants/", R.drawable.achyranthesbidentata));
        arrayList.add(new listItem("쑥_artemisiaprinceps", "/data1/LJH/plants/", R.drawable.artemisiaprinceps));
        arrayList.add(new listItem("씀바귀_ixeridiumdentatum", "/data1/LJH/plants/", R.drawable.ixeridiumdentatum));
        arrayList.add(new listItem("역기_yeogkkipul", "/data1/LJH/plants/", R.drawable.yeogkkipul));
        arrayList.add(new listItem("원추리_hemerocallis", "/data1/LJH/plants/", R.drawable.hemerocallis));
        arrayList.add(new listItem("유채_brassicanapus", "/data1/LJH/plants/", R.drawable.brassicanapus));
        arrayList.add(new listItem("이끼류_sortofmoss", "/data1/LJH/plants/", R.drawable.sortofmoss));
        arrayList.add(new listItem("점나도나물_cerastium", "/data1/LJH/plants/", R.drawable.cerastium));
        arrayList.add(new listItem("제비꽃_violamandshurica", "/data1/LJH/plants/", R.drawable.violamandshurica));
        arrayList.add(new listItem("질경이_plantagoasiatica", "/data1/LJH/plants/", R.drawable.plantagoasiatica));
        arrayList.add(new listItem("큰개불알풀_veronicapersica", "/data1/LJH/plants/", R.drawable.veronicapersica));
        arrayList.add(new listItem("클로바_clova", "/data1/LJH/plants/", R.drawable.clova));
        arrayList.add(new listItem("튤립_tulipa", "/data1/LJH/plants/", R.drawable.tulipa));
        arrayList.add(new listItem("피막이풀_hydrocotyle", "/data1/LJH/plants/", R.drawable.hydrocotyle));


    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_layout, parent, false);

        TextView title_textView = convertView.findViewById(R.id.name);
        ImageView image_imageView = convertView.findViewById(R.id.img);

        title_textView.setText(arrayList.get(position).getName());
//        image_imageView.setImageDrawable(mContext.getResources().getDrawable(arrayList.get(position).getResource()));
        RequestOptions myOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
        Glide.with(mContext).load(arrayList.get(position).getResource()).apply(myOptions).into(image_imageView);


        return convertView;
    }
}
