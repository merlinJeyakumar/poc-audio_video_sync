package com.data.webservices;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.functions.Function;
import retrofit2.HttpException;
import com.domain.IAPIExceptionMapper;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * original in http://bytes.babbel.com/en/articles/2016-03-16-retrofit2-rxjava-error-handling.html
 */
public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    private final RxJava3CallAdapterFactory original;
    private final IAPIExceptionMapper errorDecode;

    private RxErrorHandlingCallAdapterFactory(IAPIExceptionMapper errorDecode) {
        this.errorDecode = errorDecode;
        original = RxJava3CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create(IAPIExceptionMapper errorDecode) {
        return new RxErrorHandlingCallAdapterFactory(errorDecode);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public CallAdapter get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit), errorDecode);
    }

    private static class RxCallAdapterWrapper implements CallAdapter {
        private final Retrofit retrofit;
        private final CallAdapter wrapped;
        IAPIExceptionMapper errorDecode;

        public RxCallAdapterWrapper(Retrofit retrofit, CallAdapter wrapped, IAPIExceptionMapper errorDecode) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
            this.errorDecode = errorDecode;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @SuppressWarnings({"unchecked", "NullableProblems"})
        @Override
        public Object adapt(Call call) {
            Object adaptedCall = wrapped.adapt(call);

            if (adaptedCall instanceof Completable) {

                return ((Completable) adaptedCall).onErrorResumeNext(new Function<Throwable, CompletableSource>() {
                    @Override
                    public CompletableSource apply(Throwable throwable) throws Exception {

                        return Completable.error(asRetrofitException(throwable));
                    }
                });
            }

            if (adaptedCall instanceof Single) {

                return ((Single) adaptedCall).onErrorResumeNext(new Function<Throwable, SingleSource>() {
                    @Override
                    public SingleSource apply(Throwable throwable) throws Exception {
                        return Single.error(asRetrofitException((Throwable) throwable));
                    }
                });
            }

            if (adaptedCall instanceof Observable) {
                return ((Observable) adaptedCall).onErrorResumeNext(new Function<Throwable, ObservableSource>() {
                    @Override
                    public ObservableSource apply(Throwable throwable) throws Exception {

                        return Observable.error(asRetrofitException(throwable));
                    }
                });
            }
            throw new RuntimeException("Observable Type not supported");
        }

        private Throwable asRetrofitException(Throwable throwable) {

            throwable.printStackTrace();

            // We had non-200 http error
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                return errorDecode.decodeHttpException(httpException);
            }
            // A network error happened
            if (throwable instanceof IOException) {
                return errorDecode.decodeIOException((IOException) throwable);
            }
            // We don't know what happened. We need to simply convert to an unknown error
            return errorDecode.decodeUnexpectedException(throwable);
        }
    }
}