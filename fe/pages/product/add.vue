<template>
  <v-row>
    <v-col cols="10" offset="1">
      <v-card class="mb-2">
        <v-toolbar :color="$vuetify.theme.themes.dark.primary" dark>ADD USER</v-toolbar>
        <v-card-text>
          <v-breadcrumbs :items="breadcrumbs" class="pa-0"></v-breadcrumbs>
          <v-form ref="form">
            <v-text-field
              name="productCode"
              label="Product Code"
              type="text"
              :rules="rules.productCode"
              v-model="form.product_code"
            />
            <v-text-field
              name="productName"
              label="Product Name"
              type="text"
              :rules="rules.productName"
              v-model="form.product_name"
            />
            <v-file-input
              name="thumbnail"
              label="Thumbnail"
              :rules="rules.thumbnail"
              v-model="form.thumbnail"
              accept="image/*"
            />
            <v-text-field
              name="price"
              label="Price"
              type="number"
              :rules="rules.price"
              v-model="form.price"
            />
            <v-text-field
              name="stock"
              label="Stock"
              type="number"
              :rules="rules.stock"
              v-model="form.stock"
            />
            <v-select
              v-model="form.category_id"
              :items="category"
              label="Category"
              :rules="rules.category"
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
          <v-btn dark :color="$vuetify.theme.themes.dark.secondary" to="/product" color="secondary">Back</v-btn>
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
    title: "Add Product",
  },
  data() {
    return {
      breadcrumbs: [
        { text: "Product", to: "/product", disabled: false, exact: true },
        { text: "Add", disabled: true },
      ],
      btnSaveDisable: false,
      status: ["active", "inactive"],
      category: [],
      form: {
        product_code: "",
        product_name: "",
        thumbnail: "",
        stock: 0,
        category_id: "",
        price: "",
        status: "",
      },
      rules: {
        productCode: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Product Code" }),
        ],
        productName: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Product Name" }),
        ],
        status: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Status" }),
        ],
        thumbnail: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Thumbnail" }),
        ],
        price: [(v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Price" })],
        stock: [(v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Stock" })],
        category: [
          (v) => !!v || this.$t("FIELD_IS_REQUIRED", { field: "Category" }),
        ],
      },
    };
  },
  methods: {
    async doSave() {
      if (this.$refs.form.validate()) {
        this.btnSaveDisable = true;

        let formData = new FormData();
        formData.append('thumbnail', this.form.thumbnail);

        let productData = {
          product_code: this.form.product_code,
          product_name: this.form.product_name,
          stock: this.form.stock,
          category_id: this.form.category_id,
          price: this.form.price,
          status: this.form.status
        };

        formData.append('product', new Blob([JSON.stringify(productData)], {
          type: 'application/json'
        }));

        await this.$axios
            .$post("/product", formData, {
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            })
          .then(() => {
            this.$router.push({
              name: `product___${this.$i18n.locale}`,
              params: {
                type: "success",
                message: "ADD_SUCCESS",
                title: this.form.product_code,
              },
            });
          })
          .catch((error) => {
            console.log(error);
          });

        this.btnSaveDisable = false;
      }
    },
    getCategory() {
      this.$axios
        .$get(`/category`)
        .then((response) => {
          const { data } = response;
          data.forEach((cat) => {
            this.category.push({
              value: cat.id,
              text: cat.category_name,
            });
          });
          if (this.form.category_id) {
            const categoryExists = this.category.some(item => item.value === this.form.category_id);
            if (!categoryExists) {
              this.form.category_id = '';
            }
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  mounted() {
    this.getCategory();
  },
  watch: {
    category(newCategory) {
      console.log(newCategory)
      if (newCategory.length && this.form.category_id) {
        const categoryExists = newCategory.some(item => item.value === this.form.category_id);
        if (!categoryExists) {
          this.form.category_id = '';
        }
      }
    }
  }
};
</script>
