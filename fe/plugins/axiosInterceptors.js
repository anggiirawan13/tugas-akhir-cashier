export default ({ $axios, store, redirect }) => {
  $axios.onRequest((config) => {
    if (store.getters["auth/authenticated"]) {
      config.headers[
        "Authorization"
      ] = `Bearer ${store.state.auth.access_token}`;
    }
  });

  $axios.onResponse((response) => {
    const { data } = response;
    // if (!data.success) {
    //   store.commit("auth/setLogout");
    //
    //   return redirect("/logout");
    // }
  });

  // FOR REFRESH TOKEN (IN PROGRESS)
  // $axios.onResponseError(err => {
  //     if (err.response.status === 401) {
  //         return $axios.$post('/auth/refresh-token', {
  //             refreshToken: store.state.auth.refresh_token
  //         })
  //         .then(response => {
  //             store.commit('auth/setLogin', {
  //                 access_token: response.data.access_token,
  //                 refresh_token:response.data.refresh_token,
  //                 fullname: response.data.fullname
  //             })

  //             const originalRequest = err.config
  //             originalRequest.headers['Authorization'] = `Bearer ${response.data.access_token}`

  //             return $axios(originalRequest)
  //         })
  //         .catch((err) => {
  //             if (err.response.data.message === 'REFRESH_TOKEN_EXPIRED' || err.response.data.message === 'REFRESH_TOKEN_INVALID') {
  //                 store.commit('auth/setLogout')

  //                 return redirect('/login')
  //             }
  //         })
  //     }
  // })
};
