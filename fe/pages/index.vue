<template>
  <v-row>
    <v-col cols="8" class="frame-content px-6">
      <Product />
    </v-col>
    <v-col cols="4" class="frame-content white">
      <Order />
    </v-col>
  </v-row>
</template>

<script>
import Order from "~/components/Order.vue";
import Product from "~/components/Product.vue";

export default {
  components: {Order, Product},
  middleware: ["authenticated"],
  head: {
    title: "App",
  },
};
</script>
