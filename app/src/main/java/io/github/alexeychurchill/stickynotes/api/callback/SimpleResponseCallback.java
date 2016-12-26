package io.github.alexeychurchill.stickynotes.api.callback;

import android.content.Context;
import android.widget.Toast;

import io.github.alexeychurchill.stickynotes.R;
import io.github.alexeychurchill.stickynotes.model.ServiceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Simple response callback
 */

public class SimpleResponseCallback implements Callback<ServiceResponse<Object>> {
    private Context mContext;

    public SimpleResponseCallback(Context context) {
        this.mContext = context;
    }

    @Override
    public void onResponse(Call<ServiceResponse<Object>> call, Response<ServiceResponse<Object>> response) {
        if (mContext == null) {
            return;
        }
        if (!response.isSuccessful()) {
            String message = response.message()
                    .concat(" (")
                    .concat(String.valueOf(response.code()))
                    .concat(")");
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        ServiceResponse<Object> serviceResponse = response.body();
        Toast.makeText(mContext, serviceResponse.getMessage(), Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onFailure(Call<ServiceResponse<Object>> call, Throwable t) {
        if (mContext == null) {
            return;
        }
        Toast.makeText(mContext, R.string.text_failure, Toast.LENGTH_SHORT)
                .show();
    }
}
