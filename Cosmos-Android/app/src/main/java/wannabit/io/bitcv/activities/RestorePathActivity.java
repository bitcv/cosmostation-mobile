package wannabit.io.bitcv.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseActivity;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dao.Account;
import wannabit.io.bitcv.dao.Balance;
import wannabit.io.bitcv.network.ApiClient;
import wannabit.io.bitcv.network.res.ResBnbAccountInfo;
import wannabit.io.bitcv.network.res.ResLcdAccountInfo;
import wannabit.io.bitcv.network.res.ResLcdKavaAccountInfo;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;
import wannabit.io.bitcv.task.UserTask.GenerateAccountTask;
import wannabit.io.bitcv.task.UserTask.OverrideAccountTask;
import wannabit.io.bitcv.utils.WDp;
import wannabit.io.bitcv.utils.WKey;
import wannabit.io.bitcv.utils.WUtil;

import static wannabit.io.bitcv.base.BaseChain.AKASH_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BAND_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BNB_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BNB_TEST;
import static wannabit.io.bitcv.base.BaseChain.CERTIK_MAIN;
import static wannabit.io.bitcv.base.BaseChain.CERTIK_TEST;
import static wannabit.io.bitcv.base.BaseChain.COSMOS_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IOV_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IOV_TEST;
import static wannabit.io.bitcv.base.BaseChain.IRIS_MAIN;
import static wannabit.io.bitcv.base.BaseChain.KAVA_MAIN;
import static wannabit.io.bitcv.base.BaseChain.KAVA_TEST;
import static wannabit.io.bitcv.base.BaseChain.OK_TEST;
import static wannabit.io.bitcv.base.BaseChain.SECRET_MAIN;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_AKASH;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_CERTIK;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_IOV;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_IOV_TEST;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_KAVA;
import static wannabit.io.bitcv.base.BaseConstant.TOKEN_SECRET;

public class RestorePathActivity extends BaseActivity implements TaskListener {


    private String              mHdSeed;
    private String              mEntropy;
    private int                 mWordSize;
    private BaseChain           mChain;

    private Toolbar                 mToolbar;
    private RecyclerView            mRecyclerView;
    private NewWalletAdapter        mNewWalletAdapter;

    private boolean                 mIsNewBip44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_path);

        mToolbar           = findViewById(R.id.tool_bar);
        mRecyclerView       = findViewById(R.id.recycler);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mNewWalletAdapter = new NewWalletAdapter();
        mRecyclerView.setAdapter(mNewWalletAdapter);

        mHdSeed = getIntent().getStringExtra("HDseed");
        mEntropy =  getIntent().getStringExtra("entropy");
        mChain = BaseChain.getChain(getIntent().getStringExtra("chain"));
        mWordSize = getIntent().getIntExtra("size", 24);
        mIsNewBip44 = getIntent().getBooleanExtra("bip44", false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void onGenAccount(int path) {
        onShowWaitDialog();
        new GenerateAccountTask(getBaseApplication(), mChain, this, mIsNewBip44).execute(""+path, mEntropy, ""+mWordSize);
    }

    private void onOverrideAccount(Account account, int path) {
        new OverrideAccountTask(getBaseApplication(), mChain, account, this, mIsNewBip44).execute(""+path, mEntropy, ""+mWordSize);
    }

    @Override
    public void onTaskResponse(TaskResult result) {
        if(isFinishing()) return;
        if (result.taskType == BaseConstant.TASK_INIT_ACCOUNT) {
            if(result.isSuccess) {
                onStartMainActivity(0);
            }

        } else if (result.taskType == BaseConstant.TASK_OVERRIDE_ACCOUNT) {
            if(result.isSuccess) {
                onStartMainActivity(0);
            }
        }
    }



    private class NewWalletAdapter extends RecyclerView.Adapter<NewWalletAdapter.NewWalletHolder> {

        @NonNull
        @Override
        public NewWalletHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new NewWalletHolder(getLayoutInflater().inflate(R.layout.item_new_wallet, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final NewWalletHolder holder, @SuppressLint("RecyclerView") final int position) {
            String address = WKey.getDpAddressWithPath(mHdSeed, mChain, position, mIsNewBip44);
            holder.newPath.setText(WDp.getPath(mChain, position, mIsNewBip44));
            holder.newAddress.setText(address);
            final Account temp = getBaseDao().onSelectExistAccount(address, mChain);
            if (temp == null) {
                holder.newState.setText(getString(R.string.str_ready));
                holder.newState.setTextColor(getResources().getColor(R.color.colorWhite));
                if (mChain.equals(COSMOS_MAIN)) {
                    holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgCosmos));
                } else if (mChain.equals(IRIS_MAIN)) {
                    holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgIris));
                } else if (mChain.equals(BNB_MAIN) || mChain.equals(BNB_TEST)) {
                    holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgBinance));
                } else if (mChain.equals(KAVA_MAIN) || mChain.equals(KAVA_TEST)) {
                    holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgKava));
                } else if (mChain.equals(IOV_MAIN) || mChain.equals(IOV_TEST)) {
                    holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgStarname));
                } else if (mChain.equals(BAND_MAIN)) {
                    holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgBand));
                } else if (mChain.equals(CERTIK_MAIN) || mChain.equals(CERTIK_TEST)) {
                    holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgCertik));
                } else if (mChain.equals(OK_TEST)) {
                    holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgOkex));
                } else if (mChain.equals(AKASH_MAIN)) {
                    holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgAkash));
                } else if (mChain.equals(SECRET_MAIN)) {
                    holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgSecret));
                }
            } else  {
                if(temp.hasPrivateKey) {
                    holder.newState.setText(getString(R.string.str_imported));
                    holder.newState.setTextColor(getResources().getColor(R.color.colorGray1));
                    holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBg));
                } else {
                    holder.newState.setText(getString(R.string.str_override));
                    holder.newState.setTextColor(getResources().getColor(R.color.colorWhite));
                    if (mChain.equals(COSMOS_MAIN)) {
                        holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgCosmos));
                    } else if (mChain.equals(IRIS_MAIN)) {
                        holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgIris));
                    } else if (mChain.equals(BNB_MAIN) || mChain.equals(BNB_TEST)) {
                        holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgBinance));
                    } else if (mChain.equals(KAVA_MAIN)|| mChain.equals(KAVA_TEST)) {
                        holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgKava));
                    } else if (mChain.equals(IOV_MAIN) || mChain.equals(IOV_TEST)) {
                        holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgStarname));
                    } else if (mChain.equals(BAND_MAIN)) {
                        holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgBand));
                    } else if (mChain.equals(OK_TEST)) {
                        holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgOkex));
                    } else if (mChain.equals(CERTIK_MAIN) || mChain.equals(CERTIK_TEST)) {
                        holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgCertik));
                    } else if (mChain.equals(AKASH_MAIN)) {
                        holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgAkash));
                    } else if (mChain.equals(SECRET_MAIN)) {
                        holder.cardNewWallet.setCardBackgroundColor(getResources().getColor(R.color.colorTransBgSecret));
                    }
                }
            }
            holder.cardNewWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.newState.getText().toString().equals(getString(R.string.str_ready))) {
                        onGenAccount(position);
                    } else if (holder.newState.getText().toString().equals(getString(R.string.str_imported))) {
                        Toast.makeText(getBaseContext(), getString(R.string.str_already_imported_key), Toast.LENGTH_SHORT).show();

                    } else {
                        onOverrideAccount(temp, position);
                    }
                }
            });

            if (mChain.equals(COSMOS_MAIN)) {
                holder.atomLayer.setVisibility(View.VISIBLE);
                holder.atomAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 6, 6));
                ApiClient.getCosmosChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResLcdAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResLcdAccountInfo> call, Response<ResLcdAccountInfo> response) {
                        if(response.isSuccessful() && response.body() != null) {
                            ArrayList<Balance> balance = WUtil.getBalancesFromLcd(-1, response.body());
                            if(balance != null && balance.size() > 0 && balance.get(0) != null)
                                holder.atomAmount.setText(WDp.getDpAmount2(getBaseContext(), balance.get(0).balance, 6, 6));
                        }
                    }
                    @Override
                    public void onFailure(Call<ResLcdAccountInfo> call, Throwable t) { }
                });

            } else if (mChain.equals(IRIS_MAIN)) {
                holder.irisLayer.setVisibility(View.VISIBLE);
                holder.irisAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 18, 18));
                ApiClient.getIrisChain(getBaseContext()).getBankInfo(address).enqueue(new Callback<ResLcdAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResLcdAccountInfo> call, Response<ResLcdAccountInfo> response) {
                        if(response.isSuccessful() && response.body() != null && response.body().value.coins != null) {
                            ArrayList<Balance> balance = WUtil.getBalancesFromLcd(-1, response.body());
                            if(balance != null && balance.size() > 0 && balance.get(0) != null)
                                holder.irisAmount.setText(WDp.getDpAmount2(getBaseContext(), balance.get(0).balance, 18, 18));
                        }
                    }
                    @Override
                    public void onFailure(Call<ResLcdAccountInfo> call, Throwable t) { }
                });

            } else if (mChain.equals(BNB_MAIN)) {
                holder.bnbLayer.setVisibility(View.VISIBLE);
                holder.bnbAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 0, 8));
                ApiClient.getBnbChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResBnbAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResBnbAccountInfo> call, Response<ResBnbAccountInfo> response) {
                        if(response.isSuccessful() && response.body() != null && response.body().balances != null) {
                            for (ResBnbAccountInfo.BnbBalance balance:response.body().balances) {
                                if (balance.symbol.equals(BaseConstant.TOKEN_BNB)) {
                                    holder.bnbAmount.setText(WDp.getDpAmount2(getBaseContext(), new BigDecimal(balance.free), 0, 8));
                                    break;
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResBnbAccountInfo> call, Throwable t) { }
                });

            } else if (mChain.equals(KAVA_MAIN)) {
                holder.kavaLayer.setVisibility(View.VISIBLE);
                holder.kavaAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 0, 6));
                ApiClient.getKavaChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResLcdKavaAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResLcdKavaAccountInfo> call, Response<ResLcdKavaAccountInfo> response) {
                        ArrayList<Balance> balances = WUtil.getBalancesFromKavaLcd(-1, response.body());
                        holder.kavaAmount.setText(WDp.getDpAmount2(getBaseContext(), WDp.getAvailableCoin(balances, TOKEN_KAVA), 6, 6));
                    }

                    @Override
                    public void onFailure(Call<ResLcdKavaAccountInfo> call, Throwable t) { }
                });

            } else if (mChain.equals(IOV_MAIN)) {
                holder.iovLayer.setVisibility(View.VISIBLE);
                holder.iovAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 6, 6));
                ApiClient.getIovChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResLcdAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResLcdAccountInfo> call, Response<ResLcdAccountInfo> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<Balance> balance = WUtil.getBalancesFromLcd(-1, response.body());
                            if(balance != null && balance.size() > 0 && balance.get(0) != null) {
                                holder.iovAmount.setText(WDp.getDpAmount2(getBaseContext(), WDp.getAvailableCoin(balance, TOKEN_IOV), 6, 6));
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResLcdAccountInfo> call, Throwable t) {}
                });


            } else if (mChain.equals(BAND_MAIN)) {
                holder.bandLayer.setVisibility(View.VISIBLE);
                holder.bandAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 6, 6));
                ApiClient.getBandChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResLcdAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResLcdAccountInfo> call, Response<ResLcdAccountInfo> response) {
                        if(response.isSuccessful() && response.body() != null) {
                            ArrayList<Balance> balance = WUtil.getBalancesFromLcd(-1, response.body());
                            if(balance != null && balance.size() > 0 && balance.get(0) != null)
                                holder.bandAmount.setText(WDp.getDpAmount2(getBaseContext(), balance.get(0).balance, 6, 6));
                        }
                    }
                    @Override
                    public void onFailure(Call<ResLcdAccountInfo> call, Throwable t) { }
                });

            } else if (mChain.equals(CERTIK_MAIN)) {
                holder.certikLayer.setVisibility(View.VISIBLE);
                holder.certikAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 6, 6));
                ApiClient.getCertikChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResLcdAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResLcdAccountInfo> call, Response<ResLcdAccountInfo> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<Balance> balance = WUtil.getBalancesFromLcd(-1, response.body());
                            if(balance != null && balance.size() > 0 && balance.get(0) != null)
                                holder.certikAmount.setText(WDp.getDpAmount2(getBaseContext(), WDp.getAvailableCoin(balance, TOKEN_CERTIK), 6, 6));
                        }
                    }
                    @Override
                    public void onFailure(Call<ResLcdAccountInfo> call, Throwable t) { }
                });

            } else if (mChain.equals(BNB_TEST)) {
                holder.bnbLayer.setVisibility(View.VISIBLE);
                holder.bnbAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 0, 8));
                ApiClient.getBnbTestChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResBnbAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResBnbAccountInfo> call, Response<ResBnbAccountInfo> response) {
                        if(response.isSuccessful() && response.body() != null && response.body().balances != null) {
                            for (ResBnbAccountInfo.BnbBalance balance:response.body().balances) {
                                if (balance.symbol.equals(BaseConstant.TOKEN_BNB)) {
                                    holder.bnbAmount.setText(WDp.getDpAmount2(getBaseContext(), new BigDecimal(balance.free), 0, 8));
                                    break;
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResBnbAccountInfo> call, Throwable t) { }
                });

            } else if (mChain.equals(KAVA_TEST)) {
                holder.kavaLayer.setVisibility(View.VISIBLE);
                holder.kavaAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 0, 6));
                ApiClient.getKavaTestChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResLcdKavaAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResLcdKavaAccountInfo> call, Response<ResLcdKavaAccountInfo> response) {
                        ArrayList<Balance> balances = WUtil.getBalancesFromKavaLcd(-1, response.body());
                        holder.kavaAmount.setText(WDp.getDpAmount2(getBaseContext(), WDp.getAvailableCoin(balances, TOKEN_KAVA), 6, 6));

                    }

                    @Override
                    public void onFailure(Call<ResLcdKavaAccountInfo> call, Throwable t) { }
                });

            } else if (mChain.equals(IOV_TEST)) {
                holder.iovLayer.setVisibility(View.VISIBLE);
                holder.iovAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 6, 6));
                ApiClient.getIovTestChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResLcdAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResLcdAccountInfo> call, Response<ResLcdAccountInfo> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<Balance> balance = WUtil.getBalancesFromLcd(-1, response.body());
                            if(balance != null && balance.size() > 0 && balance.get(0) != null)
                                holder.iovAmount.setText(WDp.getDpAmount2(getBaseContext(), WDp.getAvailableCoin(balance, TOKEN_IOV_TEST), 6, 6));
                        }
                    }
                    @Override
                    public void onFailure(Call<ResLcdAccountInfo> call, Throwable t) { }
                });

            }
            //yongjoo
//            else if (mChain.equals(OK_TEST)) {
//                holder.okLayer.setVisibility(View.VISIBLE);
//                holder.okAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 0, 8));
//                ApiClient.getOkTestChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResLcdAccountInfo>() {
//                    @Override
//                    public void onResponse(Call<ResLcdAccountInfo> call, Response<ResLcdAccountInfo> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            ArrayList<Balance> balance = WUtil.getBalancesFromLcd(-1, response.body());
//                            if(balance != null && balance.size() > 0 && balance.get(0) != null)
//                                holder.okAmount.setText(WDp.getDpAmount2(getBaseContext(), WDp.getAvailableCoin(balance, TOKEN_OK_TEST), 0, 8));
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<ResLcdAccountInfo> call, Throwable t) { }
//                });
//
//            }
            else if (mChain.equals(CERTIK_TEST)) {
                holder.certikLayer.setVisibility(View.VISIBLE);
                holder.certikAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 6, 6));
                ApiClient.getCertikTestChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResLcdAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResLcdAccountInfo> call, Response<ResLcdAccountInfo> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<Balance> balance = WUtil.getBalancesFromLcd(-1, response.body());
                            if(balance != null && balance.size() > 0 && balance.get(0) != null)
                                holder.certikAmount.setText(WDp.getDpAmount2(getBaseContext(), WDp.getAvailableCoin(balance, TOKEN_CERTIK), 6, 6));
                        }
                    }
                    @Override
                    public void onFailure(Call<ResLcdAccountInfo> call, Throwable t) { }
                });

            } else if (mChain.equals(AKASH_MAIN)) {
                holder.akashLayer.setVisibility(View.VISIBLE);
                holder.akashAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 6, 6));
                ApiClient.getAkashChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResLcdKavaAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResLcdKavaAccountInfo> call, Response<ResLcdKavaAccountInfo> response) {
                        ArrayList<Balance> balances = WUtil.getBalancesFromKavaLcd(-1, response.body());
                        holder.akashAmount.setText(WDp.getDpAmount2(getBaseContext(), WDp.getAvailableCoin(balances, TOKEN_AKASH), 6, 6));
                    }
                    @Override
                    public void onFailure(Call<ResLcdKavaAccountInfo> call, Throwable t) { }
                });

            } else if (mChain.equals(SECRET_MAIN)) {
                holder.secretLayer.setVisibility(View.VISIBLE);
                holder.secretAmount.setText(WDp.getDpAmount2(getBaseContext(), BigDecimal.ZERO, 6, 6));
                ApiClient.getSecretChain(getBaseContext()).getAccountInfo(address).enqueue(new Callback<ResLcdAccountInfo>() {
                    @Override
                    public void onResponse(Call<ResLcdAccountInfo> call, Response<ResLcdAccountInfo> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ArrayList<Balance> balance = WUtil.getBalancesFromLcd(-1, response.body());
                            if (balance != null && balance.size() > 0 && balance.get(0) != null)
                                holder.secretAmount.setText(WDp.getDpAmount2(getBaseContext(), WDp.getAvailableCoin(balance, TOKEN_SECRET), 6, 6));
                        }
                    }
                    @Override
                    public void onFailure(Call<ResLcdAccountInfo> call, Throwable t) { }
                });

            }

        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class NewWalletHolder extends RecyclerView.ViewHolder {
            CardView cardNewWallet;
            RelativeLayout atomLayer, photonLayer, irisLayer, bnbLayer, kavaLayer, iovLayer, bandLayer, okLayer, certikLayer, akashLayer, secretLayer;
            TextView newPath, newState, newAddress, atomAmount, photonAmount, irisAmount, bnbAmount, kavaAmount, iovAmount, bandAmount, okAmount, certikAmount, akashAmount, secretAmount;

            public NewWalletHolder(View v) {
                super(v);
                cardNewWallet       = itemView.findViewById(R.id.card_new_wallet);
                newPath             = itemView.findViewById(R.id.new_path);
                newState            = itemView.findViewById(R.id.new_state);
                newAddress          = itemView.findViewById(R.id.new_address);
                atomLayer           = itemView.findViewById(R.id.atom_layer);
                atomAmount          = itemView.findViewById(R.id.atom_amount);
                photonLayer         = itemView.findViewById(R.id.photon_layer);
                photonAmount        = itemView.findViewById(R.id.photon_amount);
                irisLayer           = itemView.findViewById(R.id.iris_layer);
                irisAmount          = itemView.findViewById(R.id.iris_amount);
                bnbLayer            = itemView.findViewById(R.id.bnb_layer);
                bnbAmount           = itemView.findViewById(R.id.bnb_amount);
                kavaLayer           = itemView.findViewById(R.id.kava_layer);
                kavaAmount          = itemView.findViewById(R.id.kava_amount);
                iovLayer            = itemView.findViewById(R.id.iov_layer);
                iovAmount           = itemView.findViewById(R.id.iov_amount);
                bandLayer           = itemView.findViewById(R.id.band_layer);
                bandAmount          = itemView.findViewById(R.id.band_amount);
                okLayer             = itemView.findViewById(R.id.ok_layer);
                okAmount            = itemView.findViewById(R.id.ok_amount);
                certikLayer         = itemView.findViewById(R.id.certik_layer);
                certikAmount        = itemView.findViewById(R.id.certik_amount);
                akashLayer          = itemView.findViewById(R.id.akash_layer);
                akashAmount         = itemView.findViewById(R.id.akash_amount);
                secretLayer         = itemView.findViewById(R.id.secret_layer);
                secretAmount        = itemView.findViewById(R.id.secret_amount);
            }
        }
    }
}
