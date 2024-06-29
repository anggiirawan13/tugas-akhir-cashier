<template>
  <v-row>
    <v-col cols="10" offset="1">
      <v-card class="mb-2">
        <v-toolbar color="primary" dark>EDIT PRODUCT</v-toolbar>
        <v-card-text>
          <v-alert v-if="message" color="red lighten-2" dark>{{
            $t(message)
          }}</v-alert>
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
          <v-btn to="/product" color="secondary">Back</v-btn>
          <v-spacer />
          <v-btn @click="doSave" color="primary" :loading="btnSaveDisable"
            >Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
import login from "~/pages/login.vue";

export default {
  middleware: ["admin"],
  head: {
    title: "Edit Product",
  },
  asyncData({ params }) {
    return {
      id: params.id,
    };
  },
  data() {
    return {
      breadcrumbs: [
        { text: "Product", to: "/product", disabled: false, exact: true },
        { text: "Edit", disabled: true },
      ],
      btnSaveDisable: false,
      message: "",
      category: [],
      status: ["active", "inactive"],
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
      this.message = "";
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
            .$put(`/product/${this.id}`, formData, {
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            })
          .then(() => {
            this.$router.push({
              name: `product___${this.$i18n.locale}`,
              params: {
                type: "success",
                message: "UPDATE_SUCCESS",
                title: this.form.title,
              },
            });
          })
          .catch(() => {
            this.$router.push({
              name: `product___${this.$i18n.locale}`,
              params: {
                type: "error",
                message: "UPDATE_FAILED",
                title: this.form.title,
              },
            });
          });

        this.btnSaveDisable = false;
      }
    },
    async getData() {
      await this.$axios
        .$get(`/product/${this.id}`, this.form)
        .then((response) => {
          let product = response.data;
          this.form.product_code = product.product_code;
          this.form.product_name = product.product_name;
          this.form.thumbnail = product.thumbnail;
          this.form.stock = product.stock;
          this.form.category_id = product.category_id;
          this.form.price = product.price;
          this.form.status = product.status;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    async getCategory() {
      await this.$axios
        .$get(`/category`)
        .then((response) => {
          const { data } = response;
          this.category = data.map(item => ({ value: item.id, text: item.category_name }));
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // setCategoryId() {
    //   if (this.category.length && this.form.category_id) {
    //     const categoryExists = this.category.filter(item => {
    //       if(item.value === parseInt(this.form.category_id)) {
    //         return item;
    //       }
    //     });
    //
    //     if (!categoryExists) {
    //       this.form.category_id = '';
    //     } else {
    //       this.form.category_id = categoryExists[0].value;
    //     }
    //   }
    // }
  },
  async mounted() {
    await this.getCategory();
    await this.getData();
  },
};
</script>
