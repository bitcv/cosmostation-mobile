package wannabit.io.bitcv.activities.chains.starname;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseActivity;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.dialog.Dialog_Wallet_for_Starname;
import wannabit.io.bitcv.model.StarNameResource;
import wannabit.io.bitcv.utils.WKey;
import wannabit.io.bitcv.utils.WLog;
import wannabit.io.bitcv.utils.WUtil;

import static wannabit.io.bitcv.base.BaseChain.BAC_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BAND_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BNB_MAIN;
import static wannabit.io.bitcv.base.BaseChain.COSMOS_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IOV_MAIN;
import static wannabit.io.bitcv.base.BaseChain.IRIS_MAIN;
import static wannabit.io.bitcv.base.BaseChain.KAVA_MAIN;

public class StarNameResourceAddActivity extends BaseActivity implements View.OnClickListener {

    private Button mCancel, mConfirm;
    private LinearLayout mWallet, mScan, mPaste;
    private EditText mUserInput;
    private ImageView mChainImg;
    private TextView mChainName;


    private StarNameResource mStarNameResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starname_resource_add);

        mCancel  = findViewById(R.id.btn_cancel);
        mConfirm  = findViewById(R.id.btn_confirm);
        mWallet  = findViewById(R.id.btn_wallet);
        mScan  = findViewById(R.id.btn_qr);
        mPaste  = findViewById(R.id.btn_paste);

        mUserInput  = findViewById(R.id.user_inputs);
        mChainImg  = findViewById(R.id.chainImg);
        mChainName  = findViewById(R.id.chainName);

        mStarNameResource = getIntent().getParcelableExtra("resource");
        mChainImg.setImageDrawable(WUtil.getStarNameChainImg(getBaseContext(), mStarNameResource));
        mChainName.setText(WUtil.getStarNameChainName(mStarNameResource));

        if (!TextUtils.isEmpty(mStarNameResource.resource)) {
            mUserInput.setText(mStarNameResource.resource);
        }

        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mWallet.setOnClickListener(this);
        mScan.setOnClickListener(this);
        mPaste.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onHideKeyboard();
        if (v.equals(mCancel)) {
            finish();

        } else if (v.equals(mConfirm)) {
            final String userinput = mUserInput.getText().toString().trim();

            if (TextUtils.isEmpty(userinput)) {
                Toast.makeText(getBaseContext(), R.string.error_invalid_address_pubkey, Toast.LENGTH_SHORT).show();
                return;

            } else if (WUtil.getBaseChainWithUri(mStarNameResource.uri) != null){
                BaseChain chain = WUtil.getBaseChainWithUri(mStarNameResource.uri);
                if (chain.equals(COSMOS_MAIN) && (!userinput.startsWith("cosmos1") || !WKey.isValidBech32(userinput))) {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address_pubkey, Toast.LENGTH_SHORT).show();
                    return;

                } else if (chain.equals(IRIS_MAIN) && (!userinput.startsWith("iaa1") || !WKey.isValidBech32(userinput))) {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address_pubkey, Toast.LENGTH_SHORT).show();
                    return;

                } else if (chain.equals(KAVA_MAIN) && (!userinput.startsWith("kava1") || !WKey.isValidBech32(userinput))) {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address_pubkey, Toast.LENGTH_SHORT).show();
                    return;

                } else if (chain.equals(BAND_MAIN) && (!userinput.startsWith("band1") || !WKey.isValidBech32(userinput))) {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address_pubkey, Toast.LENGTH_SHORT).show();
                    return;

                } else if (chain.equals(BNB_MAIN) && (!userinput.startsWith("bnb1") || !WKey.isValidBech32(userinput))) {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address_pubkey, Toast.LENGTH_SHORT).show();
                    return;

                } else if (chain.equals(IOV_MAIN) && (!userinput.startsWith("star1") || !WKey.isValidBech32(userinput))) {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address_pubkey, Toast.LENGTH_SHORT).show();
                    return;

                }else if (chain.equals(BAC_MAIN) && (!userinput.startsWith("bac1") || !WKey.isValidBech32(userinput))) {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address_pubkey, Toast.LENGTH_SHORT).show();
                    return;

                }
            }
            Intent result = getIntent();
            mStarNameResource.resource = userinput;
            result.putExtra("resource", mStarNameResource);
            setResult(Activity.RESULT_OK, result);
            finish();

        } else if (v.equals(mWallet)) {
            if (WUtil.getBaseChainWithUri(mStarNameResource.uri) == null) {
                Toast.makeText(getBaseContext(), R.string.error_not_support_cosmostation, Toast.LENGTH_SHORT).show();
                return;
            }
            if (getBaseDao().onSelectAccountsByChain(WUtil.getBaseChainWithUri(mStarNameResource.uri)).size() == 0) {
                Toast.makeText(getBaseContext(), R.string.error_no_wallet_this_chain, Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString("chainUri", mStarNameResource.uri);
            Dialog_Wallet_for_Starname dialog = Dialog_Wallet_for_Starname.newInstance(bundle);
            dialog.setCancelable(true);
            getSupportFragmentManager().beginTransaction().add(dialog, "dialog").commitNowAllowingStateLoss();

        } else if (v.equals(mScan)) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientationLocked(true);
            integrator.initiateScan();

        } else if (v.equals(mPaste)) {
            ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            if(clipboard.getPrimaryClip() != null && clipboard.getPrimaryClip().getItemCount() > 0) {
                String userPaste = clipboard.getPrimaryClip().getItemAt(0).coerceToText(this).toString().trim();
                if(TextUtils.isEmpty(userPaste)) {
                    Toast.makeText(this, R.string.error_clipboard_no_data, Toast.LENGTH_SHORT).show();
                    return;
                }
                mUserInput.setText(userPaste);

            } else {
                Toast.makeText(this, R.string.error_clipboard_no_data, Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onChoiceStarnameResourceAddress(String address) {
        WLog.w("address " + address);
        mUserInput.setText(address);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                String addr = WUtil.formatAddress(result.getContents().trim());
                mUserInput.setText(addr);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
