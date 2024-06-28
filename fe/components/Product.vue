<template>
  <section id="product">
    <v-row align="center">
      <v-col cols="10">
        <v-autocomplete
          label="Product"
          placeholder="Start typing to search"
          :search-input.sync="search"
          :loading="isLoading"
          :items="itemSearch"
          item-text="product_name"
          item-value="id"
          v-model="selectedSearch"
          return-object
          hide-no-data
        >
        </v-autocomplete>
      </v-col>
      <v-col cols="2">
        <v-menu>
          <template v-slot:activator="{ on: category }">
            <v-btn v-on="category" color="primary"> Category </v-btn>
          </template>
          <v-list>
            <v-list-item-group>
              <v-list-item @change="updateCategoryId(false)">
                <v-list-item-title>All</v-list-item-title>
              </v-list-item>
              <v-list-item
                  v-for="(cat, index) in category"
                  :key="index"
                  :value="cat.id"
                  :disabled="cat.id === categoryId"
                  @change="updateCategoryId(cat.id)"
              >
                <v-list-item-title>{{
                    cat.category_name
                }}</v-list-item-title>
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </v-menu>
      </v-col>
    </v-row>
    <v-row>
      <v-col v-for="(product, index) in filteredProduct" :key="index" cols="2">
        <v-card
          @click="addToCart(product.id)"
          :title="product.product_name"
          :ripple="true"
        >
          <v-card-actions>
            <v-img
              :src="product.thumbnail"
            ></v-img>
          </v-card-actions>
          <v-card-text align="center" class="product-name">
            {{ product.product_name }}
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </section>
</template>

<script>
import { mapState, mapActions } from "vuex";

export default {
  data() {
    return {
      search: null,
      isLoading: false,
      itemSearch: [],
      selectedSearch: null,
    };
  },
  methods: {
    ...mapActions({
      updateCategoryId: "product/updateCategoryId",
      addToCart: "cart/addToCart",
      getProduct: "product/getProduct",
      getCategory: "product/getCategory",
      searchProduct: "product/searchProduct",
      resetProduct: "product/resetProduct",
    }),
    resetSearchCategory() {
      this.updateCategoryId(0);
    },
  },
  computed: {
    filteredProduct() {
      if (this.categoryId) {
        return this.product.filter(
          (product) => product.category_id === this.categoryId
        );
      }

      return this.product;
    },
    ...mapState("product", {
      product: "product",
      category: "category",
      categoryId: "categoryId",
    }),
  },
  watch: {
    search() {
      this.isLoading = true;

      if (this.search) {
        setTimeout(() => {
          this.itemSearch = this.product.filter((e) => {
            this.resetSearchCategory();

            return e.product_name;
          });
        }, 1000);
      } else {
        this.resetProduct();
        this.search = null;
        this.itemSearch = [];
      }

      this.isLoading = false;
    },
    selectedSearch(product) {
      if (product) {
        let result = this.product.filter(
          (prod) => prod.product_name === product.product_name
        );
        this.searchProduct(result);
      }
    },
  },
  mounted() {
    if (this.product.length <= 0) {
      this.getProduct();
    }

    if (this.category.length <= 0) {
      this.getCategory();
    }
  },
};
</script>

<style scoped>
.product-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
