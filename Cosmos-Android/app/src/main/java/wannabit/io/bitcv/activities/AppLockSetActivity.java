package wannabit.io.bitcv.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import wannabit.io.bitcv.R;
import wannabit.io.bitcv.base.BaseActivity;
import wannabit.io.bitcv.base.BaseConstant;
import wannabit.io.bitcv.dialog.Dialog_LockTime;

public class AppLockSetActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar         mToolbar;
    private FrameLayout     mBtnUsingAppLock, mBtnUsingFingerprint, mBtnAppLockTime;
    private SwitchCompat    mSwitchUsingAppLock, mSwitchUsingFingerprint;
    private TextView        mTvAppLockTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock_set);
        mToolbar                    = findViewById(R.id.tool_bar);
        mBtnUsingAppLock            = findViewById(R.id.card_using_applock);
        mBtnUsingFingerprint        = findViewById(R.id.card_using_fingerprint);
        mBtnAppLockTime             = findViewById(R.id.card_applock_time);
        mSwitchUsingAppLock         = findViewById(R.id.switch_using_applock);
        mSwitchUsingFingerprint     = findViewById(R.id.switch_fingerprint);
        mTvAppLockTime              = findViewById(R.id.applock_time_text);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBtnUsingAppLock.setOnClickListener(this);
        mBtnUsingFingerprint.setOnClickListener(this);
        mBtnAppLockTime.setOnClickListener(this);

        onUpdateView();
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

    private void onUpdateView() {
        mSwitchUsingAppLock.setChecked(getBaseDao().getUsingAppLock());
        mSwitchUsingFingerprint.setChecked(getBaseDao().getUsingFingerPrint());
        mTvAppLockTime.setText(getBaseDao().getAppLockLeaveTimeString(getBaseContext()));
        if(getBaseDao().getUsingAppLock()) {
            mBtnAppLockTime.setVisibility(View.VISIBLE);
            FingerprintManagerCompat mFingerprintManagerCompat = FingerprintManagerCompat.from(this);
            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) &&
                    mFingerprintManagerCompat.isHardwareDetected() &&
                    mFingerprintManagerCompat.hasEnrolledFingerprints()) {
                mBtnUsingFingerprint.setVisibility(View.VISIBLE);
            } else {
                mBtnUsingFingerprint.setVisibility(View.GONE);
            }

        } else {
            mBtnAppLockTime.setVisibility(View.GONE);
            mBtnUsingFingerprint.setVisibility(View.GONE);
        }


    }

    public void onUpdateLockTime(int time) {
        getBaseDao().setAppLockTriggerTime(time);
        onUpdateView();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBtnUsingAppLock)) {
            if(getBaseDao().getUsingAppLock()) {
                Intent intent = new Intent(AppLockSetActivity.this, PasswordCheckActivity.class);
                intent.putExtra(BaseConstant.CONST_PW_PURPOSE, BaseConstant.CONST_PW_SIMPLE_CHECK);
                startActivityForResult(intent, BaseConstant.CONST_PW_SIMPLE_CHECK);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);


            } else {
                if(getBaseDao().onHasPassword()) {
                    getBaseDao().setUsingAppLock(true);
                    onUpdateView();
                } else {
                    Toast.makeText(getBaseContext(), getString(R.string.error_no_password), Toast.LENGTH_SHORT).show();
                }
            }

        } else if (v.equals(mBtnUsingFingerprint)) {
            getBaseDao().setUsingFingerPrint(!getBaseDao().getUsingFingerPrint());
            onUpdateView();

        } else if (v.equals(mBtnAppLockTime)) {
            Dialog_LockTime timeUpdate = Dialog_LockTime.newInstance();
            timeUpdate.setCancelable(true);
            getSupportFragmentManager().beginTransaction().add(timeUpdate, "dialog").commitNowAllowingStateLoss();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BaseConstant.CONST_PW_SIMPLE_CHECK && resultCode == Activity.RESULT_OK) {
            getBaseDao().setUsingAppLock(false);
        }
        onUpdateView();
    }
}
