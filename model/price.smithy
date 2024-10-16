$version: "2.0"

namespace com.jt

use aws.protocols#restJson1

@restJson1
service PriceControllerApi {
    version: "v1"
    operations: [
        GetPrice
    ]
    resources: [
        Price
    ]
}

resource Price {
    identifiers: {
        id: String
    }
}

@http(method: "GET", uri: "/api/prices")
@readonly
operation GetPrice {
    input: GetPriceInput
    output: GetPriceOutput
}

structure GetPriceInput {
    @httpQuery("brandId")
    @required
    brandId: Integer

    @required
    @httpQuery("productId")
    productId: Integer

    @required
    @httpQuery("applicationDate")
    applicationDate: Timestamp
}

structure GetPriceOutput {
    message: String

    @range(min: 0.0)
    price: Double

    productId: Integer

    priceList: Integer
}
