package wannabit.io.bitcv.fragment.chains.starname;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import wannabit.io.bitcv.activities.chains.starname.ReplaceStarNameActivity;
import wannabit.io.bitcv.activities.chains.starname.StarNameResourceAddActivity;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseFragment;
import wannabit.io.bitcv.dialog.Dialog_StarName_Resource;
import wannabit.io.bitcv.model.StarNameResource;
import wannabit.io.bitcv.utils.WUtil;

import static wannabit.io.bitcv.utils.WUtil.STARNAME;

public class ReplaceStarName0Fragment extends BaseFragment implements View.OnClickListener {
    public final static int SELECT_ADD_CHAIN    = 9700;
    public final static int SELECT_ADD_ADDRESS  = 9701;

    private Button mCancelBtn, mNextBtn;
    private RecyclerView mRecyclerView;
    private ResourceAdapter mResourceAdapter;
    public ArrayList<StarNameResource> mResources = new ArrayList();

    public static ReplaceStarName0Fragment newInstance(Bundle bundle) {
        ReplaceStarName0Fragment fragment = new ReplaceStarName0Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_replace_starname_0, container, false);
        mCancelBtn = rootView.findViewById(R.id.btn_cancel);
        mNextBtn = rootView.findViewById(R.id.btn_next);
        mRecyclerView   = rootView.findViewById(R.id.recycler);
        mCancelBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mResourceAdapter = new ResourceAdapter();
        mRecyclerView.setAdapter(mResourceAdapter);

        return rootView;
    }

    @Override
    public void onRefreshTab() {
        mResources = getSActivity().mMyNameAccount.resources;
        if (mResources == null || mResources.size() == 0) {
            mResources = new ArrayList();
            StarNameResource initData = new StarNameResource(STARNAME, getSActivity().mAccount.address);
            mResources.add(initData);
        }
        mResourceAdapter.notifyDataSetChanged();
    }

    private ReplaceStarNameActivity getSActivity() {
        return (ReplaceStarNameActivity)getBaseActivity();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mCancelBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mNextBtn)) {
            ArrayList<StarNameResource> tempResources = new ArrayList();
            for (StarNameResource resource:mResources) {
                if (!TextUtils.isEmpty(resource.resource) && !TextUtils.isEmpty(resource.uri)){
                    tempResources.add(resource);
                }
            }
            if (tempResources.size() == 0) {
                Toast.makeText(getSActivity(), R.string.error_no_address_added, Toast.LENGTH_SHORT).show();
                return;
            }
            getSActivity().mResources = tempResources;
            getSActivity().onNextStep();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_ADD_CHAIN && resultCode == Activity.RESULT_OK) {
            StarNameResource temp = data.getParcelableExtra("resource");
//            WLog.w("SELECT_ADD_CHAIN " + temp.uri);
            Intent intent = new Intent(getSActivity(), StarNameResourceAddActivity.class);
            intent.putExtra("resource", temp);
            startActivityForResult(intent, SELECT_ADD_ADDRESS);
            getSActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);

        } else if (requestCode == SELECT_ADD_ADDRESS && resultCode == Activity.RESULT_OK) {
            StarNameResource temp = data.getParcelableExtra("resource");
//            WLog.w("SELECT_ADD_ADDRESS " + temp.uri + "  " + temp.resource);
            int position = -1;
            for (int i = 0 ; i < mResources.size(); i ++) {
                if (mResources.get(i).uri.equals(temp.uri)) {
                    position = i;
                    break;
                }
            }

            if (position >= 0) {
                mResources.set(position, temp);
            } else {
                mResources.add(temp);
            }
            mResourceAdapter.notifyDataSetChanged();
        }
    }


    private class ResourceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_RESOURCE          = 1;
        private static final int TYPE_ADD               = 2;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            if (viewType == TYPE_RESOURCE) {
                return new ResourceHolder(getLayoutInflater().inflate(R.layout.item_manage_starname_resource, viewGroup, false));
            } else if(viewType == TYPE_ADD) {
                return new ResourceAddHolder(getLayoutInflater().inflate(R.layout.item_manage_starname_add, viewGroup, false));
            }
            return null;

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            if (getItemViewType(position) == TYPE_RESOURCE) {
                final StarNameResource resource = mResources.get(position);
                final ResourceHolder holder = (ResourceHolder)viewHolder;
                holder.itemChainImg.setImageDrawable(WUtil.getStarNameChainImg(getContext(), resource));
                holder.itemChainName.setText(WUtil.getStarNameChainName(resource));
                holder.itemChainAddress.setText(resource.resource);
                holder.itemRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getSActivity(), StarNameResourceAddActivity.class);
                        intent.putExtra("resource", resource);
                        startActivityForResult(intent, SELECT_ADD_ADDRESS);
                        getSActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
                    }
                });
                if (mResources.size() <= 1) {
                    holder.itemBtnRemove.setVisibility(View.GONE);
                } else {
                    holder.itemBtnRemove.setVisibility(View.VISIBLE);
                }

                holder.itemBtnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mResources.remove(position);
                        mResourceAdapter.notifyDataSetChanged();
                    }
                });

            } else if (getItemViewType(position) == TYPE_ADD) {
                final ResourceAddHolder holder = (ResourceAddHolder)viewHolder;
                holder.itemBtnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("resources", mResources);
                        Dialog_StarName_Resource dialog = Dialog_StarName_Resource.newInstance(bundle);
                        dialog.setTargetFragment(ReplaceStarName0Fragment.this, SELECT_ADD_CHAIN);
                        dialog.show(getFragmentManager(), "dialog");
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (mResources.size() >= 14) {
                return mResources.size();
            } else {
                return mResources.size() + 1;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mResources.size() >= 14) {
                return TYPE_RESOURCE;
            } else {
                if (position < mResources.size()) {
                    return TYPE_RESOURCE;
                } else {
                    return TYPE_ADD;
                }
            }
        }



        public class ResourceAddHolder extends RecyclerView.ViewHolder {
            CardView itemRoot;
            Button itemBtnAdd;

            public ResourceAddHolder(@NonNull View itemView) {
                super(itemView);
                itemRoot = itemView.findViewById(R.id.card_root);
                itemBtnAdd = itemView.findViewById(R.id.btn_add);
            }
        }

        public class ResourceHolder extends RecyclerView.ViewHolder {
            CardView itemRoot;
            ImageView itemChainImg;
            TextView itemChainName, itemChainAddress, itemBtnRemove;

            public ResourceHolder(@NonNull View itemView) {
                super(itemView);
                itemRoot         = itemView.findViewById(R.id.card_root);
                itemChainImg     = itemView.findViewById(R.id.chain_img);
                itemChainName    = itemView.findViewById(R.id.chain_name);
                itemChainAddress = itemView.findViewById(R.id.chain_address);
                itemBtnRemove    = itemView.findViewById(R.id.btn_remove);
            }
        }
    }
}
