<template>
  <v-container>
    <v-btn dark :color="$vuetify.theme.themes.dark.secondary" @click="generatePDF" class="mb-2">
      Download PDF
    </v-btn>
    <v-card>
      <v-card-title>Laporan Pembelian</v-card-title>
      <v-card-text>
        <v-simple-table>
          <template v-slot:default>
            <thead>
            <tr>
              <th class="text-left">No</th>
              <th class="text-left">Tanggal Pembelian</th>
              <th class="text-left">Nama Barang</th>
              <th class="text-left">Harga</th>
              <th class="text-left">Jumlah</th>
              <th class="text-left">Total Harga</th>
              <th class="text-left">Total</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(order, index) in orders" :key="order.uuid">
              <td>{{ index + 1 }}</td>
              <td>{{ formatDate(order.created_at) }}</td>
              <td>
                <template v-for="(item, idx) in order.items">
                  {{ item.product.product_name }}
                  <br />
                </template>
              </td>
              <td>
                <template v-for="(item, idx) in order.items">
                  {{ item.price }}
                  <br />
                </template>
              </td>
              <td>
                <template v-for="(item, idx) in order.items">
                  {{ item.quantity }}
                  <br />
                </template>
              </td>
              <td>
                <template v-for="(item, idx) in order.items">
                  {{ item.quantity * item.price }}
                  <br />
                </template>
              </td>
              <td>{{ totalCost(order.items) }}</td>
            </tr>
            </tbody>
          </template>
        </v-simple-table>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import pdfMake from 'pdfmake/build/pdfmake';
import pdfFonts from 'pdfmake/build/vfs_fonts';

pdfMake.vfs = pdfFonts.pdfMake.vfs;

export default {
  data() {
    return {
      orders: []
    };
  },
  mounted() {
    this.fetchOrdersAndGeneratePDF();
  },
  methods: {
    async fetchOrdersAndGeneratePDF() {
      try {
        const response = await this.$axios.$get('/order');
        this.orders = response.data;
      } catch (error) {
        console.error('Error fetching orders:', error);
      }
    },
    generatePDF() {
      const documentDefinition = {
        content: [
          {text: 'Laporan Pembelian', style: 'header'},
          '\n',
          {
            table: {
              headerRows: 1,
              widths: ['auto', 'auto', '*', 'auto', 'auto', 'auto', 'auto'],
              body: [
                ['No', 'Tanggal Pembelian', 'Nama Barang', 'Harga', 'Jumlah', 'Total Harga', 'Total'],
                ...this.orders.map((order, index) => [
                  index + 1,
                  this.formatDate(order.created_at),
                  this.formatItems(order.items),
                  this.formatItemPrices(order.items),
                  this.formatItemQuantities(order.items),
                  this.formatItemTotalPrices(order.items),
                  this.totalCost(order.items)
                ])
              ]
            }
          }
        ],
        styles: {
          header: {
            fontSize: 18,
            bold: true
          }
        }
      };

      pdfMake.createPdf(documentDefinition).download("report.pdf");
    },
    formatDate(dateString) {
      const options = {year: 'numeric', month: '2-digit', day: '2-digit'};
      return new Date(dateString).toLocaleDateString('id-ID', options);
    },
    formatItems(items) {
      return items.map(item => `${item.product.product_name}`).join('\n');
    },
    formatItemPrices(items) {
      return items.map(item => item.price.toFixed(2)).join('\n');
    },
    formatItemQuantities(items) {
      return items.map(item => item.quantity).join('\n');
    },
    formatItemTotalPrices(items) {
      return items.map(item => (item.quantity * item.price).toFixed(2)).join('\n');
    },
    totalCost(items) {
      return items.reduce((total, item) => total + item.quantity * item.price, 0);
    }
  }
};
</script>

<style scoped>
.text-left {
  text-align: left;
}
</style>
