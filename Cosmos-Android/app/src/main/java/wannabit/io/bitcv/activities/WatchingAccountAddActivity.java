package wannabit.io.bitcv.activities;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseActivity;
import wannabit.io.bitcv.base.BaseChain;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dialog.Dialog_Choice_Certik;
import wannabit.io.bitcv.dialog.Dialog_Choice_Iov;
import wannabit.io.bitcv.dialog.Dialog_Choice_Kava;
import wannabit.io.bitcv.task.TaskListener;
import wannabit.io.bitcv.task.TaskResult;
import wannabit.io.bitcv.task.UserTask.GenerateEmptyAccountTask;
import wannabit.io.bitcv.utils.WKey;
import wannabit.io.bitcv.utils.WUtil;

import static wannabit.io.bitcv.base.BaseChain.AKASH_MAIN;
import static wannabit.io.bitcv.base.BaseChain.BAC_MAIN;
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
import static wannabit.io.bitcv.base.BaseChain.SUPPORT_CHAINS;

public class WatchingAccountAddActivity extends BaseActivity implements View.OnClickListener, TaskListener {

    private Toolbar mToolbar;
    private EditText mInput;
    private Button mCancel, mNext;
    private LinearLayout mBtnQr, mBtnPaste, mBtnHistory;

    private String mUserInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching_account_add);

        mToolbar        = findViewById(R.id.tool_bar);
        mInput          = findViewById(R.id.et_address);
        mCancel         = findViewById(R.id.btn_cancel);
        mNext           = findViewById(R.id.btn_next);
        mBtnQr          = findViewById(R.id.btn_qr);
        mBtnPaste       = findViewById(R.id.btn_paste);
        mBtnHistory     = findViewById(R.id.btn_history);
        mBtnHistory.setVisibility(View.GONE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCancel.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mBtnQr.setOnClickListener(this);
        mBtnPaste.setOnClickListener(this);
        mBtnHistory.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if(v.equals(mCancel)) {
            onBackPressed();

        } else if (v.equals(mNext)) {
            mUserInput = mInput.getText().toString().trim();
            if (mUserInput.startsWith("cosmos1")) {
                if (WKey.isValidBech32(mUserInput)) {
                    onGenNewAccount(COSMOS_MAIN, mUserInput);
                    return;

                } else {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mUserInput.startsWith("iaa1")) {
                if (WKey.isValidBech32(mUserInput)) {
                    onGenNewAccount(IRIS_MAIN, mUserInput);
                    return;

                } else {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mUserInput.startsWith("bnb1")) {
                if (WKey.isValidBech32(mUserInput)) {
                    onGenNewAccount(BNB_MAIN, mUserInput);
                    return;

                } else {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mUserInput.startsWith("bac1")) {
                if (WKey.isValidBech32(mUserInput)) {
                    onGenNewAccount(BAC_MAIN, mUserInput);
                    return;

                } else {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mUserInput.startsWith("kava1")) {
                if (WKey.isValidBech32(mUserInput)) {
                    if (SUPPORT_CHAINS().contains(KAVA_TEST)) {
                        Dialog_Choice_Kava dialog = Dialog_Choice_Kava.newInstance(null);
                        dialog.setCancelable(false);
                        getSupportFragmentManager().beginTransaction().add(dialog, "dialog").commitNowAllowingStateLoss();
                        return;

                    } else {
                        onGenNewAccount(KAVA_MAIN, mUserInput);
                        return;
                    }

                } else {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mUserInput.startsWith("star1")) {
                if (WKey.isValidBech32(mUserInput)) {
                    if (SUPPORT_CHAINS().contains(IOV_TEST)) {
                        Dialog_Choice_Iov dialog = Dialog_Choice_Iov.newInstance(null);
                        dialog.setCancelable(false);
                        getSupportFragmentManager().beginTransaction().add(dialog, "dialog").commitNowAllowingStateLoss();
                        return;

                    } else {
                        onGenNewAccount(IOV_MAIN, mUserInput);
                        return;
                    }

                } else {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mUserInput.startsWith("tbnb1")) {
                if (WKey.isValidBech32(mUserInput)) {
                    onGenNewAccount(BNB_TEST, mUserInput);
                    return;

                } else {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mUserInput.startsWith("band1")) {
                if (WKey.isValidBech32(mUserInput)) {
                    onGenNewAccount(BAND_MAIN, mUserInput);
                    return;

                } else {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mUserInput.startsWith("okexchain1")) {
                if (WKey.isValidBech32(mUserInput)) {
                    onGenNewAccount(OK_TEST, mUserInput);
                    return;

                } else {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mUserInput.startsWith("certik1")) {
                if (WKey.isValidBech32(mUserInput)) {
                    if (SUPPORT_CHAINS().contains(CERTIK_TEST)) {
                        Dialog_Choice_Certik dialog = Dialog_Choice_Certik.newInstance(null);
                        dialog.setCancelable(false);
                        getSupportFragmentManager().beginTransaction().add(dialog, "dialog").commitNowAllowingStateLoss();
                        return;

                    } else {
                        onGenNewAccount(CERTIK_MAIN, mUserInput);
                        return;
                    }

                } else {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (mUserInput.startsWith("secret1")) {
                if (WKey.isValidBech32(mUserInput)) {
                    onGenNewAccount(SECRET_MAIN, mUserInput);
                } else {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                }

            } else if (mUserInput.startsWith("akash1")) {
                if (WKey.isValidBech32(mUserInput)) {
                    onGenNewAccount(AKASH_MAIN, mUserInput);
                } else {
                    Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getBaseContext(), R.string.error_invalid_address, Toast.LENGTH_SHORT).show();
                return;
            }

        } else if (v.equals(mBtnQr)) {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientationLocked(true);
            integrator.initiateScan();

        } else if (v.equals(mBtnPaste)) {
            ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            if(clipboard.getPrimaryClip() != null && clipboard.getPrimaryClip().getItemCount() > 0) {
                String userPaste = clipboard.getPrimaryClip().getItemAt(0).coerceToText(this).toString().trim();
                if(TextUtils.isEmpty(userPaste)) {
                    Toast.makeText(this, R.string.error_clipboard_no_data, Toast.LENGTH_SHORT).show();
                    return;
                }
                mInput.setText(userPaste);
                mInput.setSelection(mInput.getText().length());

            } else {
                Toast.makeText(this, R.string.error_clipboard_no_data, Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onChoiceNet(BaseChain chain) {
        super.onChoiceNet(chain);
        onGenNewAccount(chain, mUserInput);
    }

    public void onGenNewAccount(BaseChain chain, String address) {
        onShowWaitDialog();
        new GenerateEmptyAccountTask(getBaseApplication(), WatchingAccountAddActivity.this).execute(chain.getChain(), address);

    }

    @Override
    public void onTaskResponse(TaskResult result) {
        if(isFinishing()) return;
        onHideWaitDialog();
        if (result.taskType == BaseConstant.TASK_INIT_EMPTY_ACCOUNT) {
            if(result.isSuccess) {
                onStartMainActivity(0);
            } else {
                if(result.errorCode == 7001) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_already_imported_address), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.error_import_errer), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                String addr = WUtil.formatAddress(result.getContents().trim());
                mInput.setText(addr);
                mInput.setSelection(mInput.getText().length());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
