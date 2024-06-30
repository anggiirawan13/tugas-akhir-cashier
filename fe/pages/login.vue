<template>
  <v-row>
    <v-col cols="10" offset="1" md="4" offset-md="4">
      <v-card class="mb-2">
        <v-toolbar :color="$vuetify.theme.themes.dark.primary" dark>LOGIN</v-toolbar>
        <v-card-text>
          <v-alert v-if="message" color="red lighten-2" dark>{{
            $t(message)
          }}</v-alert>
          <v-form>
            <v-text-field
              name="username"
              label="Username"
              type="text"
              v-model="form.username"
            />
            <v-text-field
              name="password"
              label="Password"
              type="password"
              v-model="form.password"
            />
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn dark :color="$vuetify.theme.themes.dark.secondary"  @click="doLogin" :loading="btnLoginDisable">
            Login
          </v-btn>
        </v-card-actions>
      </v-card>
      <p>
        Kamu belum punya akun ?
        <v-btn dark :color="$vuetify.theme.themes.dark.secondary"  to="/register" plain>Register</v-btn>
      </p>
    </v-col>
  </v-row>
</template>

<script>
import { mapMutations } from "vuex";

export default {
  middleware: ["unauthenticated"],
  head: {
    title: "Login",
  },
  data() {
    return {
      btnLoginDisable: false,
      message: "",
      form: {
        username: "",
        password: "",
      },
    };
  },
  mounted() {
    if (this.$route.params.message === "AUTH_IS_REQUIRED") {
      this.message = this.$route.params.message;
    }
  },
  methods: {
    ...mapMutations("auth", {
      login: "setLogin",
    }),
    storeWelcomeScreen() {
      localStorage.setItem("welcomeScreen", true);
    },
    async doLogin() {
      this.btnLoginDisable = true;

      await this.$axios
        .$post("/user/login", this.form)
        .then((response) => {
          const { data } = response;
          if (response.success) {
            if (!localStorage.welcomeScreen) this.storeWelcomeScreen();
            this.login({
              access_token: data.access_token,
              refresh_token: data.refresh_token,
              fullname: data.fullname,
            });

            this.$router.push("/dashboard");
          }
        })
        .catch((error) => {
          this.message = error.response.data.message;
        });

      this.btnLoginDisable = false;
    },
  },
};
</script>
