<template>
  <v-row>
    <v-col cols="10" offset="1" md="4" offset-md="4">
      <v-card class="mb-2">
        <v-toolbar :color="$vuetify.theme.themes.dark.primary" dark>REGISTER</v-toolbar>
        <v-card-text>
          <v-alert v-if="message" color="red lighten-2" dark>{{
            $t(message)
          }}</v-alert>
          <v-form ref="form">
            <v-text-field
              name="fullname"
              label="Fullname"
              type="text"
              :rules="rules.fullname"
              v-model="form.fullname"
            />
            <v-text-field
              name="username"
              label="Username"
              type="text"
              :rules="rules.username"
              v-model="form.username"
            />
            <v-text-field
              name="email"
              label="Email"
              type="email"
              :rules="rules.email"
              v-model="form.email"
            />
            <v-text-field
              name="password"
              label="Password"
              type="password"
              :rules="rules.password"
              v-model="form.password"
            />
            <v-text-field
              name="retype_password"
              label="Re-Password"
              type="password"
              :rules="rules.retype_password"
              v-model="form.retype_password"
            />
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn dark :color="$vuetify.theme.themes.dark.secondary"
            @click="doRegister"
            :loading="btnRegisterDisable"
            >Register
          </v-btn>
        </v-card-actions>
      </v-card>
      <p>Kamu sudah punya akun ? <v-btn dark :color="$vuetify.theme.themes.dark.secondary"  to="/login" plain>Login</v-btn></p>
    </v-col>
  </v-row>
</template>

<script>
export default {
  middleware: ["unauthenticated"],
  head: {
    title: "Register",
  },
  data() {
    return {
      btnRegisterDisable: false,
      message: "",
      form: {
        fullname: "",
        username: "",
        email: "",
        password: "",
        retype_password: "",
      },
      rules: {
        fullname: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Fullname" }),
        ],
        username: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Username" }),
        ],
        email: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Email" }),
          (v) => /.+@.+/.test(v) || this.$t("EMAIL_INVALID"),
        ],
        password: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Password" }),
          (v) =>
            v.length >= 8 ||
            this.$t("FIELD_MIN", { field: "Password", min: 8 }),
        ],
        retype_password: [
          (v) =>
            v === this.form.password ||
            this.$t("FIELD_CONFIRM", {
              confirm: "Re-Password",
              field: "Password",
            }),
        ],
      },
    };
  },
  methods: {
    async doRegister() {
      this.message = "";
      if (this.$refs.form.validate()) {
        this.btnRegisterDisable = true;

        await this.$axios
          .$post("/user/register", this.form)
          .then(() => {
            this.$router.push("/login");
          })
          .catch((error) => {
            this.message = error.response.data.message;
          });

        this.btnRegisterDisable = false;
      }
    },
  },
};
</script>
