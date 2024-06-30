<template>
  <v-row>
    <v-col cols="10" offset="1">
      <v-card class="mb-2">
        <v-toolbar :color="$vuetify.theme.themes.dark.primary" dark>ADD CATEGORY</v-toolbar>
        <v-card-text>
          <v-alert v-if="message" color="red lighten-2" dark>{{
            $t(message)
          }}</v-alert>
          <v-breadcrumbs :items="breadcrumbs" class="pa-0"></v-breadcrumbs>
          <v-form ref="form">
            <v-text-field
              name="categoryCode"
              label="Category Code"
              type="text"
              :rules="rules.categoryCode"
              v-model="form.category_code"
            />
            <v-text-field
              name="categoryName"
              label="Category Name"
              type="text"
              :rules="rules.categoryName"
              v-model="form.category_name"
            />
            <v-select
              v-model="form.status"
              :items="status"
              label="Status"
              :rules="rules.status"
            ></v-select>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-btn dark :color="$vuetify.theme.themes.dark.secondary" to="/category" color="secondary">Back</v-btn>
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
    title: "Add Category",
  },
  data() {
    return {
      breadcrumbs: [
        { text: "Category", to: "/category", disabled: false, exact: true },
        { text: "Add", disabled: true },
      ],
      btnSaveDisable: false,
      message: "",
      status: ["active", "inactive"],
      form: {
        category_code: "",
        category_name: "",
        status: "",
      },
      rules: {
        categoryCode: [
          (v) =>
            !!v || this.$t("FIELD_IS_REQUIRED", { field: "Category Code" }),
        ],
        categoryName: [
          (v) =>
            !!v || this.$t("FIELD_IS_REQUIRED", { field: "Category Name" }),
        ],
        status: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Status" }),
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
          .$post("/category", this.form)
          .then((res) => {
            this.$router.push({
              name: `category___${this.$i18n.locale}`,
              params: {
                type: "success",
                message: res.messages,
                title: this.form.category_code,
              },
            });
          })
          .catch((error) => {
            this.message = error.response.data.message;
          });

        this.btnSaveDisable = false;
      }
    },
  },
};
</script>
