<template>
  <v-row>
    <v-col cols="10" offset="1">
      <v-card class="mb-2">
        <v-toolbar :color="$vuetify.theme.themes.dark.primary" dark>EDIT USER</v-toolbar>
        <v-card-text>
          <v-alert v-if="message" color="red lighten-2" dark>{{
            $t(message)
          }}</v-alert>
          <v-breadcrumbs :items="breadcrumbs" class="pa-0"></v-breadcrumbs>
          <v-form ref="form">
            <v-text-field
              name="username"
              label="Username"
              type="text"
              :rules="rules.username"
              v-model="form.username"
            />
            <v-text-field
              name="fullname"
              label="Fullname"
              type="text"
              :rules="rules.fullname"
              v-model="form.fullname"
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
            <v-select
              v-model="form.role"
              :items="roles"
              label="Role"
              :rules="rules.role"
            ></v-select>
            <v-select
              v-model="form.status"
              :items="status"
              label="Status"
              :rules="rules.status"
            ></v-select>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn dark :color="$vuetify.theme.themes.dark.secondary" to="/user" color="secondary">Back</v-btn>
          <v-spacer />
          <v-btn dark :color="$vuetify.theme.themes.dark.secondary" @click="doSave" :loading="btnSaveDisable"
            >Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
export default {
  middleware: ["authenticated"],
  head: {
    title: "Edit User",
  },
  asyncData({ params }) {
    return {
      id: params.id,
    };
  },
  data() {
    return {
      breadcrumbs: [
        { text: "User", to: "/user", disabled: false, exact: true },
        { text: "Edit", disabled: true },
      ],
      btnSaveDisable: false,
      message: "",
      roles: ["admin", "cashier"],
      status: ["active", "inactive"],
      form: {
        username: "",
        fullname: "",
        email: "",
        password: "",
        role: "",
        status: "",
      },
      rules: {
        fullname: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Fullname" }),
        ],
        role: [(v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Role" })],
        status: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Status" }),
        ],
        email: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Email" }),
          (v) => /.+@.+/.test(v) || this.$t("EMAIL_INVALID"),
        ],
        password: [
          (v) =>
            v.length === 0 ||
            v.length >= 8 ||
            this.$t("FIELD_MIN", { field: "Password", min: 8 }),
        ],
      },
    };
  },
  methods: {
    async doSave() {
      this.message = "";
      if (this.$refs.form.validate()) {
        this.btnSaveDisable = true;

        await this.$axios
          .$put(`/user/${this.id}`, this.form)
          .then(() => {
            this.$router.push({
              name: `user___${this.$i18n.locale}`,
              params: {
                type: "success",
                message: "UPDATE_SUCCESS",
                fullname: this.form.fullname,
              },
            });
          })
          .catch(() => {
            this.$router.push({
              name: `user___${this.$i18n.locale}`,
              params: {
                type: "error",
                message: "UPDATE_FAILED",
                fullname: this.form.fullname,
              },
            });
          });

        this.btnSaveDisable = false;
      }
    },
    getData() {
      this.$axios
        .$get(`/user/${this.id}`, this.form)
        .then((response) => {
          const { data } = response;

          this.form.username = data.username;
          this.form.fullname = data.fullname;
          this.form.email = data.email;
          this.form.password = data.password;
          this.form.role = data.role;
          this.form.status = data.status;
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  mounted() {
    this.getData();
  },
};
</script>
